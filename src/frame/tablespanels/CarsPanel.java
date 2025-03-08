package frame.tablespanels;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarsPanel extends JPanel {
    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel carModelL = new JLabel("Кола:");
    JTextField carModelTf = new JTextField();

    JLabel yearOfProductionL = new JLabel("Година на производство:");
    JTextField yearOfProductionTf = new JTextField();

    JLabel horsePowerL = new JLabel("Мощност:");
    JTextField horsePowerTf = new JTextField();

    JLabel gearboxL = new JLabel("Скорости:");
    String[] gearboxOption ={"Автоматични" , "Ръчни"};
    JComboBox<String>  gearboxCb = new JComboBox<String>(gearboxOption);

    JLabel fuelL = new JLabel("Гориво:");
    String[] fuelOption ={"Бензин" , "Дизел"};
    JComboBox<String>  fuelCb = new JComboBox<String>(fuelOption);

    JLabel mileageL = new JLabel("Пробег:");
    JTextField mileageTf = new JTextField();

    JLabel priceL = new JLabel("Цена:");
    JTextField priceTf = new JTextField();

    JButton addBtn = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton editBTN = new JButton("Edit");
    JButton searchBTN = new JButton("Search");
    JButton refreshBTN = new JButton("Refresh");


    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);
    
    public CarsPanel() {
        this.setSize(400, 500);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(7, 2));
        upPanel.add(carModelL);
        upPanel.add(carModelTf);
        upPanel.add(yearOfProductionL);
        upPanel.add(yearOfProductionTf);
        upPanel.add(horsePowerL);
        upPanel.add(horsePowerTf);
        upPanel.add(gearboxL);
        upPanel.add(gearboxCb);
        upPanel.add(fuelL);
        upPanel.add(fuelCb);
        upPanel.add(mileageL);
        upPanel.add(mileageTf);
        upPanel.add(priceL);
        upPanel.add(priceTf);
        this.add(upPanel);

        midPanel.add(addBtn);
        midPanel.add(deleteButton);
        midPanel.add(editBTN);
        midPanel.add(searchBTN);
        midPanel.add(refreshBTN);
        this.add(midPanel);

        myScroll.setPreferredSize(new Dimension(350 , 150));
        downPanel.add(myScroll);
        this.add(downPanel);
        
        this.setVisible(true);
    }
}
