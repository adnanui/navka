package gui;

import manager.FinancialManager;
import model.Transaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private FinancialManager fm;
    private JLabel saldoLabel;
    private JLabel pemasukanLabel;
    private JLabel pengeluaranLabel;
    private JLabel warningLabel;
    private DefaultTableModel tableModel;
    private MiniChartPanel miniChart;

    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color NAVY = new Color(0x1E2A3A);
    private static final Color BG = new Color(0xF8FAFC);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(0x1E293B);
    private static final Color TEXT_SECONDARY = new Color(0x64748B);

    public DashboardPanel(FinancialManager fm) {
        this.fm = fm;
        setLayout(new BorderLayout());
        setBackground(BG);
        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Ringkasan keuangan bulan ini");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Center
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG);

        // Card panel
        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        cardPanel.setBackground(BG);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));

        saldoLabel = new JLabel("Rp 0");
        pemasukanLabel = new JLabel("Rp 0");
        pengeluaranLabel = new JLabel("Rp 0");

        cardPanel.add(createCard("Saldo Saat Ini", saldoLabel, GOLD, true));
        cardPanel.add(createCard("Total Pemasukan", pemasukanLabel, new Color(0x16A34A), false));
        cardPanel.add(createCard("Total Pengeluaran", pengeluaranLabel, new Color(0xDC2626), false));
        center.add(cardPanel);

        // Warning banner
        warningLabel = new JLabel("  ⚠  Pengeluaran mendekati batas budget!");
        warningLabel.setOpaque(true);
        warningLabel.setBackground(new Color(0xFEF3C7));
        warningLabel.setForeground(new Color(0x92400E));
        warningLabel.setFont(new Font("Arial", Font.BOLD, 13));
        warningLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, GOLD),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        warningLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        warningLabel.setVisible(false);

        JPanel warningWrapper = new JPanel(new BorderLayout());
        warningWrapper.setBackground(BG);
        warningWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        warningWrapper.add(warningLabel);
        center.add(warningWrapper);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomPanel.setBackground(BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Tabel
        String[] columns = {"Tanggal", "Judul", "Kategori", "Nominal", "Tipe"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0xF1F5F9));
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

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
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                        "Transaksi Terakhir",
                        0, 0,
                        new Font("Arial", Font.BOLD, 13),
                        TEXT_SECONDARY
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        bottomPanel.add(scrollPane);

        // Mini chart
        miniChart = new MiniChartPanel(fm);
        miniChart.setBackground(CARD_BG);
        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                        "Pengeluaran per Kategori",
                        0, 0,
                        new Font("Arial", Font.BOLD, 13),
                        TEXT_SECONDARY
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        chartWrapper.add(miniChart, BorderLayout.CENTER);
        bottomPanel.add(chartWrapper);

        center.add(bottomPanel);
        add(center, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor, boolean isMain) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(CARD_BG);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(TEXT_SECONDARY);

        valueLabel.setFont(new Font("Arial", Font.BOLD, isMain ? 24 : 20));
        valueLabel.setForeground(isMain ? GOLD : TEXT_PRIMARY);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(valueLabel);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        saldoLabel.setText("Rp " + String.format("%,.0f", fm.getSaldo()));
        pemasukanLabel.setText("Rp " + String.format("%,.0f", fm.getTotalPemasukan()));
        pengeluaranLabel.setText("Rp " + String.format("%,.0f", fm.getTotalPengeluaran()));

        boolean anyWarning = false;
        for (String kategori : fm.getCategoryTotals().keySet()) {
            if (fm.isNearLimit(kategori)) {
                anyWarning = true;
                warningLabel.setText("  ⚠  Pengeluaran " + kategori + " mendekati batas budget!");
                break;
            }
        }
        warningLabel.setVisible(anyWarning);

        tableModel.setRowCount(0);
        List<Transaction> list = fm.getAllTransactions();
        int count = Math.min(list.size(), 7);
        for (int i = 0; i < count; i++) {
            Transaction t = list.get(i);
            tableModel.addRow(new Object[]{
                    t.getTanggal(), t.getJudul(), t.getKategori(),
                    "Rp " + String.format("%,.0f", t.getNominal()),
                    t.getTipe()
            });
        }
        miniChart.refresh();
        repaint();
    }
}