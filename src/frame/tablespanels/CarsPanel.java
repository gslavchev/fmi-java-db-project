package frame.tablespanels;

import javax.swing.*;
import java.awt.*;

public class CarsPanel extends JFrame {
    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel carModelL = new JLabel("Кола:");
    JLabel yearOfProductionL = new JLabel("Година на производство:");
    JLabel horsePowerL = new JLabel("Мощност:");
    JLabel gearboxL = new JLabel("Скорости:");
    JLabel fuelL = new JLabel("Гориво:");
    JLabel mileageL = new JLabel("Пробег:");
    JLabel priceL = new JLabel("Цена:");

    JTextField carModelTf = new JTextField();
    JTextField yearOfProductionTf = new JTextField();
    JTextField horsePowerTf = new JTextField();
    JTextField mileageTf = new JTextField();
    JTextField priceTf = new JTextField();
    String[] gearboxOption ={"Автоматични" , "Ръчни"};
    JComboBox<String>  gearboxCb = new JComboBox<String>(gearboxOption);
    String[] fuelOption ={"Бензин" , "Дизел"};
    JComboBox<String>  fuelCb = new JComboBox<String>(fuelOption);

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

        upPanel.setLayout(new GridLayout(5, 2));
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
