package frame;

import frame.tablespanels.CarsPanel;
import frame.tablespanels.CustomersPanel;
import frame.tablespanels.SalesPanel;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    JTabbedPane tabbedPane = new JTabbedPane();

    CustomersPanel customersPanel = new CustomersPanel();
    SalesPanel salesPanel = new SalesPanel();
    CarsPanel carsPanel = new CarsPanel();

    public MyFrame() {
        this.setSize(400, 600);
        this.setName("NG MOTORSPORTS");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane.add(customersPanel , "Коли");
        tabbedPane.add(salesPanel , "Продажби");
        tabbedPane.add(carsPanel , "Клиенти");

        this.add(tabbedPane);

        this.setVisible(true);
    }
}
