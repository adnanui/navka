package gui;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private FinanceGUI mainFrame;
    private JButton activeButton = null;

    private static final Color SIDEBAR_BG = new Color(0x1E2A3A);
    private static final Color GOLD = new Color(0xF59E0B);
    private static final Color GOLD_HOVER = new Color(0xD97706);
    private static final Color TEXT_WHITE = Color.WHITE;

    public SidebarPanel(FinanceGUI mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(220, 720));
        setBackground(SIDEBAR_BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Logo area
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(0x152031));
        logoPanel.setMaximumSize(new Dimension(220, 80));
        logoPanel.setPreferredSize(new Dimension(220, 80));
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel logo = new JLabel("SIMAS");
        logo.setForeground(GOLD);
        logo.setFont(new Font("Arial", Font.BOLD, 26));
        logoPanel.add(logo);
        add(logoPanel);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x2D3F52));
        sep.setMaximumSize(new Dimension(220, 1));
        add(sep);

        add(Box.createVerticalStrut(10));

        // Nav buttons
        JButton dashBtn = createNavButton("⊞  Dashboard", "dashboard");
        JButton transaksiBtn = createNavButton("⇄  Transaksi", "transaksi");
        JButton grafikBtn = createNavButton("▦  Grafik", "grafik");
        JButton budgetBtn = createNavButton("◈  Budget", "budget");

        add(dashBtn);
        add(transaksiBtn);
        add(grafikBtn);
        add(budgetBtn);

        add(Box.createVerticalGlue());

        // Footer
        JLabel footer = new JLabel("v1.0.0");
        footer.setForeground(new Color(0x4A6080));
        footer.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(footer);

        // Set dashboard as default active
        setActive(dashBtn);
    }

    private JButton createNavButton(String label, String panelName) {
        JButton btn = new JButton(label);
        btn.setMaximumSize(new Dimension(220, 48));
        btn.setPreferredSize(new Dimension(220, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(new Color(0xCBD5E1));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (activeButton != btn) {
                    btn.setBackground(new Color(0x243447));
                    btn.setForeground(GOLD);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (activeButton != btn) {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setForeground(new Color(0xCBD5E1));
                }
            }
        });

        btn.addActionListener(e -> {
            mainFrame.showPanel(panelName);
            setActive(btn);
        });

        return btn;
    }

    private void setActive(JButton btn) {
        if (activeButton != null) {
            activeButton.setBackground(SIDEBAR_BG);
            activeButton.setForeground(new Color(0xCBD5E1));
            activeButton.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        }
        activeButton = btn;
        btn.setBackground(new Color(0x243447));
        btn.setForeground(GOLD);
        btn.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, GOLD));
    }
}