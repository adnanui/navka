package gui;

import manager.FinancialManager;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionPanel extends JPanel {
    private FinancialManager fm;
    private FinanceGUI mainFrame;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField judulField;
    private JTextField nominalField;
    private JComboBox<String> kategoriBox;
    private JComboBox<String> tipeBox;
    private JTextField tanggalField;
    private JTextField keteranganField;
    private JComboBox<String> periodeBox;
    private JTable table;
    private String editingId = null;
    private JButton simpanBtn;

    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color NAVY = new Color(0x1E2A3A);
    private static final Color BG = new Color(0xF8FAFC);
    private static final Color TEXT_PRIMARY = new Color(0x1E293B);
    private static final Color TEXT_SECONDARY = new Color(0x64748B);

    public TransactionPanel(FinancialManager fm, FinanceGUI mainFrame) {
        this.fm = fm;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(BG);
        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));
        JLabel title = new JLabel("Transaksi");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Form panel
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, GOLD),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBackground(Color.WHITE);

        judulField = createStyledField();
        nominalField = createStyledField();
        kategoriBox = new JComboBox<>(new String[]{"Makan", "Transport", "Pendidikan", "Hiburan", "Lainnya"});
        tipeBox = new JComboBox<>(new String[]{"PENGELUARAN", "PEMASUKAN"});
        tanggalField = createStyledField();
        tanggalField.setText(LocalDate.now().toString());
        keteranganField = createStyledField();

        formPanel.add(createLabel("Judul:"));
        formPanel.add(judulField);
        formPanel.add(createLabel("Nominal (Rp):"));
        formPanel.add(nominalField);
        formPanel.add(createLabel("Kategori:"));
        formPanel.add(kategoriBox);
        formPanel.add(createLabel("Tipe:"));
        formPanel.add(tipeBox);
        formPanel.add(createLabel("Tanggal (yyyy-mm-dd):"));
        formPanel.add(tanggalField);
        formPanel.add(createLabel("Keterangan:"));
        formPanel.add(keteranganField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        JButton batalEditBtn = createButton("Batal", new Color(0x94A3B8));
        batalEditBtn.addActionListener(e -> resetForm());

        simpanBtn = createButton("Simpan", GOLD);
        simpanBtn.addActionListener(e -> simpanTransaksi());

        JButton undoBtn = createButton("Undo", new Color(0xDC2626));
        undoBtn.addActionListener(e -> undo());

        btnPanel.add(batalEditBtn);
        btnPanel.add(simpanBtn);
        btnPanel.add(undoBtn);

        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(btnPanel, BorderLayout.SOUTH);

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(BG);
        formContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        formContainer.add(formWrapper);
        add(formContainer, BorderLayout.NORTH);

        // Search & filter toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        toolbar.setBackground(BG);
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 25, 5, 25));

        searchField = createStyledField();
        searchField.setPreferredSize(new Dimension(180, 32));
        searchField.addActionListener(e -> refresh());

        JButton searchBtn = createButton("Cari", NAVY);
        searchBtn.addActionListener(e -> refresh());

        periodeBox = new JComboBox<>(new String[]{"Semua", "Hari Ini", "Minggu Ini", "Bulan Ini"});
        periodeBox.addActionListener(e -> refresh());

        JButton sortNominalAsc = createSmallButton("Nominal ↑");
        sortNominalAsc.addActionListener(e -> showSorted(fm.getSortedByNominal(true)));
        JButton sortNominalDesc = createSmallButton("Nominal ↓");
        sortNominalDesc.addActionListener(e -> showSorted(fm.getSortedByNominal(false)));
        JButton sortTanggal = createSmallButton("Tanggal ↑");
        sortTanggal.addActionListener(e -> showSorted(fm.getSortedByTanggal(true)));
        JButton editBtn = createSmallButton("Edit Dipilih");
        editBtn.setBackground(new Color(0xF59E0B));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> loadSelectedToForm());

        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(new JLabel("Periode:"));
        toolbar.add(periodeBox);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(sortNominalAsc);
        toolbar.add(sortNominalDesc);
        toolbar.add(sortTanggal);
        toolbar.add(editBtn);

        // Table
        String[] columns = {"Tanggal", "Judul", "Kategori", "Nominal", "Tipe", "Saldo", "Keterangan"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0xF1F5F9));
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.setShowGrid(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String tipe = (String) tableModel.getValueAt(row, 4);
                    if ("PEMASUKAN".equals(tipe)) {
                        c.setBackground(new Color(0xF0FDF4));
                    } else {
                        c.setBackground(new Color(0xFFF5F5));
                    }
                    c.setForeground(TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE2E8F0)));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        centerPanel.add(toolbar, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(bg.equals(GOLD) ? Color.BLACK : Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0xF1F5F9));
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadSelectedToForm() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang mau diedit dulu!");
            return;
        }
        String judul = (String) tableModel.getValueAt(selectedRow, 1);
        for (Transaction t : fm.getAllTransactions()) {
            if (t.getJudul().equals(judul)) {
                editingId = t.getId();
                judulField.setText(t.getJudul());
                nominalField.setText(String.valueOf(t.getNominal()));
                kategoriBox.setSelectedItem(t.getKategori());
                tipeBox.setSelectedItem(t.getTipe());
                tanggalField.setText(t.getTanggal().toString());
                keteranganField.setText(t.getKeterangan());
                simpanBtn.setText("Update");
                simpanBtn.setBackground(new Color(0xD97706));
                break;
            }
        }
    }

    private void resetForm() {
        editingId = null;
        judulField.setText("");
        nominalField.setText("");
        keteranganField.setText("");
        tanggalField.setText(LocalDate.now().toString());
        simpanBtn.setText("Simpan");
        simpanBtn.setBackground(GOLD);
    }

    private List<Transaction> getFilteredByPeriode() {
        String periode = (String) periodeBox.getSelectedItem();
        LocalDate now = LocalDate.now();
        switch (periode) {
            case "Hari Ini": return fm.getByDateRange(now, now);
            case "Minggu Ini": return fm.getByDateRange(now.minusDays(7), now);
            case "Bulan Ini": return fm.getByDateRange(now.withDayOfMonth(1), now);
            default: return fm.getAllTransactions();
        }
    }

    private void simpanTransaksi() {
        try {
            String judul = judulField.getText();
            double nominal = Double.parseDouble(nominalField.getText());
            String kategori = (String) kategoriBox.getSelectedItem();
            String tipe = (String) tipeBox.getSelectedItem();
            LocalDate tanggal = LocalDate.parse(tanggalField.getText());
            String keterangan = keteranganField.getText();

            if (editingId != null) {
                fm.updateTransaction(editingId, judul, nominal, kategori, tipe, tanggal, keterangan);
                resetForm();
            } else {
                String id = UUID.randomUUID().toString();
                fm.addTransaction(new Transaction(id, judul, nominal, kategori, tipe, tanggal, keterangan));
            }
            mainFrame.refreshAll();
            refresh();
            judulField.setText("");
            nominalField.setText("");
            keteranganField.setText("");
            tanggalField.setText(LocalDate.now().toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid: " + ex.getMessage());
        }
    }

    private void undo() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin mau batalkan transaksi terakhir?", "Konfirmasi Undo",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Transaction t = fm.undoLast();
            if (t != null) {
                JOptionPane.showMessageDialog(this, "Transaksi dibatalkan: " + t.getJudul());
                mainFrame.refreshAll();
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada transaksi yang bisa dibatalkan.");
            }
        }
    }

    private void showSorted(List<Transaction> list) {
        tableModel.setRowCount(0);
        double runningBalance = 0;
        for (Transaction t : list) {
            if (t.getTipe().equals("PEMASUKAN")) runningBalance += t.getNominal();
            else runningBalance -= t.getNominal();
            tableModel.addRow(new Object[]{
                    t.getTanggal(), t.getJudul(), t.getKategori(),
                    "Rp " + String.format("%,.0f", t.getNominal()),
                    t.getTipe(), "Rp " + String.format("%,.0f", runningBalance),
                    t.getKeterangan()
            });
        }
    }

    public void refresh() {
        String keyword = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        double runningBalance = 0;
        List<Transaction> list = getFilteredByPeriode().stream()
                .filter(t -> t.getJudul().toLowerCase().contains(keyword) ||
                        t.getKategori().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        for (int i = list.size() - 1; i >= 0; i--) {
            Transaction t = list.get(i);
            if (t.getTipe().equals("PEMASUKAN")) runningBalance += t.getNominal();
            else runningBalance -= t.getNominal();
            tableModel.insertRow(0, new Object[]{
                    t.getTanggal(), t.getJudul(), t.getKategori(),
                    "Rp " + String.format("%,.0f", t.getNominal()),
                    t.getTipe(), "Rp " + String.format("%,.0f", runningBalance),
                    t.getKeterangan()
            });
        }
    }
}