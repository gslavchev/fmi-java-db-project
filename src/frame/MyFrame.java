package frame;

import frame.tablespanels.CarsPanel;
import frame.tablespanels.CustomersPanel;
import frame.tablespanels.SalesPanel;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    JTabbedPane tabbedPane = new JTabbedPane();

    public MyFrame() {
        this.setSize(400, 600);
        this.setName("NG MOTORSPORTS");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        CustomersPanel customersPanel = new CustomersPanel();
        tabbedPane.add(customersPanel , "Коли");

        SalesPanel salesPanel = new SalesPanel();
        tabbedPane.add(salesPanel , "Продажби");

        CarsPanel carsPanel = new CarsPanel();
        tabbedPane.add(carsPanel , "Клиенти");

        this.add(tabbedPane);

        this.setVisible(true);
    }
}
