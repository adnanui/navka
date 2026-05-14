package gui;

import manager.FinancialManager;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ChartPanel extends JPanel {
    private FinancialManager fm;
    private JComboBox<String> periodeBox;
    private BarChartPanel barChart;
    private JLabel totalPemasukanLabel;
    private JLabel totalPengeluaranLabel;
    private JLabel saldoLabel;

    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color NAVY = new Color(0x1E2A3A);
    private static final Color BG = new Color(0xF8FAFC);
    private static final Color TEXT_PRIMARY = new Color(0x1E293B);
    private static final Color TEXT_SECONDARY = new Color(0x64748B);

    public ChartPanel(FinancialManager fm) {
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

        JLabel title = new JLabel("Grafik Pengeluaran");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);

        periodeBox = new JComboBox<>(new String[]{"Bulan Ini", "Minggu Ini"});
        periodeBox.addActionListener(e -> refresh());

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setBackground(BG);
        headerRight.add(new JLabel("Periode:"));
        headerRight.add(periodeBox);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Summary cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBackground(BG);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));

        totalPemasukanLabel = new JLabel("Rp 0");
        totalPengeluaranLabel = new JLabel("Rp 0");
        saldoLabel = new JLabel("Rp 0");

        summaryPanel.add(createCard("Total Pemasukan", totalPemasukanLabel, new Color(0x16A34A)));
        summaryPanel.add(createCard("Total Pengeluaran", totalPengeluaranLabel, new Color(0xDC2626)));
        summaryPanel.add(createCard("Saldo", saldoLabel, GOLD));

        // Chart area
        barChart = new BarChartPanel(fm);
        barChart.setBackground(Color.WHITE);

        JPanel chartWrapper = new JPanel(new BorderLayout());
        chartWrapper.setBackground(BG);
        chartWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        JPanel chartCard = new JPanel(new BorderLayout());
        chartCard.setBackground(Color.WHITE);
        chartCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel chartTitle = new JLabel("Pengeluaran per Kategori");
        chartTitle.setFont(new Font("Arial", Font.BOLD, 14));
        chartTitle.setForeground(TEXT_SECONDARY);
        chartTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        chartCard.add(chartTitle, BorderLayout.NORTH);
        chartCard.add(barChart, BorderLayout.CENTER);
        chartWrapper.add(chartCard);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG);
        centerPanel.add(summaryPanel, BorderLayout.NORTH);
        centerPanel.add(chartWrapper, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xE2E8F0)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(0x64748B));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(new Color(0x1E293B));

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(valueLabel);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        totalPemasukanLabel.setText("Rp " + String.format("%,.0f", fm.getTotalPemasukan()));
        totalPengeluaranLabel.setText("Rp " + String.format("%,.0f", fm.getTotalPengeluaran()));
        saldoLabel.setText("Rp " + String.format("%,.0f", fm.getSaldo()));
        barChart.refresh();
        repaint();
    }
}