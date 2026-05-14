package gui;

import manager.FinancialManager;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BudgetPanel extends JPanel {
    private FinancialManager fm;
    private JPanel cardContainer;
    private JComboBox<String> kategoriBox;
    private JTextField limitField;

    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color NAVY = new Color(0x1E2A3A);
    private static final Color BG = new Color(0xF8FAFC);
    private static final Color TEXT_PRIMARY = new Color(0x1E293B);
    private static final Color TEXT_SECONDARY = new Color(0x64748B);

    public BudgetPanel(FinancialManager fm) {
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

        JLabel title = new JLabel("Budget");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Kelola batas pengeluaran per kategori");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Form set budget
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, GOLD),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                )
        ));

        JPanel formRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        formRow.setBackground(Color.WHITE);

        kategoriBox = new JComboBox<>(new String[]{"Makan", "Transport", "Pendidikan", "Hiburan", "Lainnya"});
        kategoriBox.setPreferredSize(new Dimension(150, 35));

        limitField = new JTextField(15);
        limitField.setPreferredSize(new Dimension(150, 35));
        limitField.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton setBtn = new JButton("Set Budget");
        setBtn.setBackground(GOLD);
        setBtn.setForeground(Color.BLACK);
        setBtn.setFont(new Font("Arial", Font.BOLD, 13));
        setBtn.setFocusPainted(false);
        setBtn.setBorderPainted(false);
        setBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBtn.setPreferredSize(new Dimension(120, 35));
        setBtn.addActionListener(e -> setBudget());

        formRow.add(new JLabel("Kategori:"));
        formRow.add(kategoriBox);
        formRow.add(new JLabel("Limit (Rp):"));
        formRow.add(limitField);
        formRow.add(setBtn);

        formWrapper.add(formRow, BorderLayout.CENTER);

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(BG);
        formContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        formContainer.add(formWrapper);
        add(formContainer, BorderLayout.NORTH);

        // Card container
        cardContainer = new JPanel(new GridLayout(0, 2, 15, 15));
        cardContainer.setBackground(BG);

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBackground(BG);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setBackground(BG);
        scrollWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        scrollWrapper.add(scrollPane);
        add(scrollWrapper, BorderLayout.CENTER);
    }

    private void setBudget() {
        try {
            String kategori = (String) kategoriBox.getSelectedItem();
            double limit = Double.parseDouble(limitField.getText());
            fm.setBudgetLimit(kategori, limit);
            limitField.setText("");
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid!");
        }
    }

    public void refresh() {
        cardContainer.removeAll();
        Map<String, Double> totals = fm.getCategoryTotals();
        Map<String, Double> limits = fm.getAllLimits();

        if (totals.isEmpty()) {
            JLabel empty = new JLabel("Belum ada data pengeluaran");
            empty.setForeground(TEXT_SECONDARY);
            empty.setFont(new Font("Arial", Font.PLAIN, 14));
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            cardContainer.add(empty);
        } else {
            for (Map.Entry<String, Double> entry : totals.entrySet()) {
                String kategori = entry.getKey();
                double total = entry.getValue();
                double limit = limits.getOrDefault(kategori, 0.0);
                cardContainer.add(createBudgetCard(kategori, total, limit));
            }
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private JPanel createBudgetCard(String kategori, double total, double limit) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Kategori name
        JLabel nameLabel = new JLabel(kategori);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_PRIMARY);

        // Amount
        String amountText = "Rp " + String.format("%,.0f", total);
        if (limit > 0) {
            amountText += " / Rp " + String.format("%,.0f", limit);
        } else {
            amountText += " (no limit)";
        }
        JLabel amountLabel = new JLabel(amountText);
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        amountLabel.setForeground(TEXT_SECONDARY);

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setBorderPainted(false);

        int percent = 0;
        Color barColor = new Color(0x16A34A);

        if (limit > 0) {
            percent = (int) Math.min((total / limit) * 100, 100);
            if (percent >= 90) {
                barColor = new Color(0xDC2626);
            } else if (percent >= 70) {
                barColor = new Color(0xF59E0B);
            }
        }

        progressBar.setValue(percent);
        progressBar.setForeground(barColor);
        progressBar.setBackground(new Color(0xF1F5F9));

        // Percent label
        JLabel percentLabel = new JLabel(limit > 0 ? percent + "% terpakai" : "Limit belum diset");
        percentLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        percentLabel.setForeground(barColor);

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(amountLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(progressBar);
        card.add(Box.createVerticalStrut(5));
        card.add(percentLabel);

        return card;
    }
}