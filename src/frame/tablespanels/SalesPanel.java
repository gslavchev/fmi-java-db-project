package frame.tablespanels;

import javax.swing.*;
import java.awt.*;

public class SalesPanel extends JPanel {
    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();
    public SalesPanel() {
        this.setSize(400, 500);
        this.setLayout(new GridLayout(3, 1));
        
        
        this.setVisible(true);
    }
}
