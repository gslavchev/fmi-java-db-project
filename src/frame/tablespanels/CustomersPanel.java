package frame.tablespanels;

import javax.swing.*;
import java.awt.*;

public class CustomersPanel extends JPanel {
    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel customerNameL = new JLabel("Име:");
    JLabel ageL = new JLabel("Възраст:");
    JLabel genderL = new JLabel("Пол:");
    JLabel cityL = new JLabel("Град:");

    JTextField customerNameTf = new JTextField();
    JTextField ageTf = new JTextField();
    JTextField cityTf = new JTextField();
    String[] genderOption ={"Мъж" , "Жена"};
    JComboBox<String>  genderCb = new JComboBox<String>(genderOption);


    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton editButton = new JButton("Edit");
    JButton searchButton = new JButton("Search");
    JButton refreshButton = new JButton("Refresh");


    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);
   
    public CustomersPanel() {
        this.setSize(400, 500);
        this.setLayout(new GridLayout(3, 1));

        upPanel.setLayout(new GridLayout(5, 2));
        upPanel.add(customerNameL);
        upPanel.add(customerNameTf);
        upPanel.add(ageL);
        upPanel.add(ageTf);
        upPanel.add(genderL);
        upPanel.add(genderCb);
        upPanel.add(cityL);
        upPanel.add(cityTf);
        this.add(upPanel);

        midPanel.add(addButton);
        midPanel.add(deleteButton);
        midPanel.add(editButton);
        midPanel.add(searchButton);
        midPanel.add(refreshButton);
        this.add(midPanel);

        myScroll.setPreferredSize(new Dimension(350 , 150));
        downPanel.add(myScroll);
        this.add(downPanel);

        this.setVisible(true);
    }
}
