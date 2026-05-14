package gui;

import manager.FinancialManager;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MiniChartPanel extends JPanel {
    private FinancialManager fm;

    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color GOLD_LIGHT = new Color(0xFDE68A);
    private static final Color TEXT_SECONDARY = new Color(0x64748B);
    private static final Color TEXT_PRIMARY = new Color(0x1E293B);

    public MiniChartPanel(FinancialManager fm) {
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
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Belum ada data", getWidth() / 2 - 45, getHeight() / 2);
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int padding = 30;
        int chartHeight = height - padding * 2;
        int barWidth = (width - padding * 2) / data.size();

        double max = data.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);

        int i = 0;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            int barHeight = (int) ((entry.getValue() / max) * chartHeight);
            int x = padding + i * barWidth;
            int y = height - padding - barHeight;
            int bw = barWidth - 8;

            GradientPaint gradient = new GradientPaint(x, y, GOLD, x, y + barHeight, GOLD_LIGHT);
            g2.setPaint(gradient);
            g2.fillRoundRect(x + 4, y, bw, barHeight, 4, 4);

            g2.setColor(TEXT_PRIMARY);
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            g2.drawString(entry.getKey(), x + 4, height - 5);

            i++;
        }
    }

    public void refresh() {
        repaint();
    }
}