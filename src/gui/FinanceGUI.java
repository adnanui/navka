package gui;

import manager.FinancialManager;
import javax.swing.*;
import java.awt.*;

public class FinanceGUI extends JFrame {
    private FinancialManager fm;
    private JPanel contentPanel;
    private DashboardPanel dashboardPanel;
    private TransactionPanel transactionPanel;
    private ChartPanel chartPanel;
    private BudgetPanel budgetPanel;

    public FinanceGUI() {
        fm = new FinancialManager();
        initUI();
    }

    private void initUI() {
        setTitle("SIMAS - Sistem Informasi Manajemen Anggaran Mahasiswa");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        SidebarPanel sidebar = new SidebarPanel(this);
        add(sidebar, BorderLayout.WEST);

        // Content area
        contentPanel = new JPanel(new CardLayout());
        dashboardPanel = new DashboardPanel(fm);
        transactionPanel = new TransactionPanel(fm, this);
        chartPanel = new ChartPanel(fm);
        budgetPanel = new BudgetPanel(fm);

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(transactionPanel, "transaksi");
        contentPanel.add(chartPanel, "grafik");
        contentPanel.add(budgetPanel, "budget");

        add(contentPanel, BorderLayout.CENTER);
        showPanel("dashboard");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    public void refreshAll() {
        dashboardPanel.refresh();
        chartPanel.refresh();
        budgetPanel.refresh();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinanceGUI::new);
    }
}