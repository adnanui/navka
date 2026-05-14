package gui;

import manager.FinancialManager;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartPanel extends JPanel {
    private FinancialManager fm;

    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color GOLD_LIGHT = new Color(0xFDE68A);
    private static final Color TEXT_PRIMARY = new Color(0x1E293B);
    private static final Color TEXT_SECONDARY = new Color(0x64748B);

    public BarChartPanel(FinancialManager fm) {
        this.fm = fm;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<String, Double> data = fm.getCategoryTotals();
        if (data.isEmpty()) {
            g2.setColor(TEXT_SECONDARY);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("Belum ada data pengeluaran", getWidth() / 2 - 100, getHeight() / 2);
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int paddingLeft = 80;
        int paddingRight = 30;
        int paddingTop = 30;
        int paddingBottom = 50;
        int chartWidth = width - paddingLeft - paddingRight;
        int chartHeight = height - paddingTop - paddingBottom;
        int barCount = data.size();
        int barWidth = chartWidth / barCount;
        int barPadding = barWidth / 4;

        double max = data.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);

        // Background grid lines
        g2.setColor(new Color(0xF1F5F9));
        for (int i = 0; i <= 4; i++) {
            int y = paddingTop + (chartHeight / 4) * i;
            g2.drawLine(paddingLeft, y, width - paddingRight, y);
        }

        // Y axis labels
        g2.setColor(TEXT_SECONDARY);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 4; i++) {
            double value = max - (max / 4) * i;
            int y = paddingTop + (chartHeight / 4) * i;
            String label = "Rp" + String.format("%,.0f", value);
            g2.drawString(label, 5, y + 4);
        }

        // Axes
        g2.setColor(new Color(0xE2E8F0));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(paddingLeft, paddingTop, paddingLeft, paddingTop + chartHeight);
        g2.drawLine(paddingLeft, paddingTop + chartHeight, width - paddingRight, paddingTop + chartHeight);

        // Bars
        int i = 0;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            int barHeight = (int) ((entry.getValue() / max) * chartHeight);
            int x = paddingLeft + i * barWidth + barPadding;
            int y = paddingTop + chartHeight - barHeight;
            int bw = barWidth - barPadding * 2;

            // Bar gradient effect
            GradientPaint gradient = new GradientPaint(
                    x, y, GOLD,
                    x, y + barHeight, GOLD_LIGHT
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(x, y, bw, barHeight, 6, 6);

            // Bar border
            g2.setColor(new Color(0xD97706));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, bw, barHeight, 6, 6);

            // Kategori label
            g2.setColor(TEXT_PRIMARY);
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            int labelX = x + (bw - fm.stringWidth(entry.getKey())) / 2;
            g2.drawString(entry.getKey(), labelX, paddingTop + chartHeight + 20);

            // Value label above bar
            g2.setColor(TEXT_PRIMARY);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            String valLabel = "Rp" + String.format("%,.0f", entry.getValue());
            int valX = x + (bw - g2.getFontMetrics().stringWidth(valLabel)) / 2;
            g2.drawString(valLabel, valX, y - 5);

            i++;
        }
    }

    public void refresh() {
        repaint();
    }
}