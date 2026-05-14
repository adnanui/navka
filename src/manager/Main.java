package manager;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import gui.FinanceGUI;

public class Main {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("Table.alternateRowColor", new Color(245, 245, 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(FinanceGUI::new);
    }
}