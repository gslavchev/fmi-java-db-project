package frame.tablespanels;

import database.access.DBConnection;
import database.tablemodel.MyModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomersPanel extends JPanel {
    Connection conn;
    PreparedStatement state;
    ResultSet result;
    int id = -1;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel customerNameL = new JLabel("Име:");
    JTextField customerNameTf = new JTextField();

    JLabel ageL = new JLabel("Възраст:");
    JTextField ageTf = new JTextField();

    JLabel genderL = new JLabel("Пол:");
    String[] genderOption ={"Мъж" , "Жена"};
    JComboBox<String>  genderCb = new JComboBox<String>(genderOption);

    JLabel cityL = new JLabel("Град:");
    JTextField cityTf = new JTextField();

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

        addButton.addActionListener(new AddActionButton());
        table.addMouseListener(new MouseAction());
        editButton.addActionListener(new EditAction());
        deleteButton.addActionListener(new DeleteAction());
        searchButton.addActionListener(new SearchAction());
        refreshButton.addActionListener(new RefreshAction());

        try {
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.setVisible(true);
    }

    public void refreshTable() throws SQLException {
        conn = DBConnection.getConnection();
        try {
            state = conn.prepareStatement("SELECT * FROM CLIENTS");
            result = state.executeQuery();
            table.setModel(new MyModel(result));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void refreshComboPerson() throws SQLException {
        genderCb.removeAllItems();

        String[] genderOptions = {"Мъж", "Жена"};
        for (String gender : genderOptions) {
            genderCb.addItem(gender);
        }
    }

    public void clearForm() {
        customerNameTf.setText("");
        ageTf.setText("");
        cityTf.setText("");
    }


    class AddActionButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                conn = DBConnection.getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String sql="INSERT INTO CLIENTS (CLIENT_NAME, CLIENT_AGE, CLIENT_GENDER, CLIENT_CITY) VALUES (?, ?, ?, ?)";

            try {
                state = conn.prepareStatement(sql);
                state.setString(1, customerNameTf.getText());
                state.setInt(2, Integer.parseInt(ageTf.getText()));
                state.setString(3, genderCb.getSelectedItem().toString());
                state.setString(4, cityTf.getText());

                state.execute();
                refreshTable();
                refreshComboPerson();
                clearForm();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class MouseAction implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            if (row != -1) {

                String customerName = table.getValueAt(row, 1).toString();
                String age = table.getValueAt(row, 2).toString();
                String gender = table.getValueAt(row, 3).toString();
                String city = table.getValueAt(row, 4).toString();

                customerNameTf.setText(customerName);
                ageTf.setText(age);
                cityTf.setText(city);

                if (gender.equals("Мъж")) {
                    genderCb.setSelectedIndex(0);
                } else {
                    genderCb.setSelectedIndex(1);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent e) {


        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    }

    class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                int clientId = (int) table.getValueAt(selectedRow, 0);

                try {
                    conn = DBConnection.getConnection();
                    String sql = "DELETE FROM CLIENTS WHERE CLIENT_ID = ?";
                    state = conn.prepareStatement(sql);
                    state.setInt(1, clientId);
                    state.executeUpdate();

                    refreshTable();
                    refreshComboPerson();
                    clearForm();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Изберете клиент за изтриване.");
            }
        }
    }

    class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                int clientId = (int) table.getValueAt(selectedRow, 0);

                String updatedName = customerNameTf.getText();
                String updatedAge = ageTf.getText();
                String updatedGender = (String) genderCb.getSelectedItem();
                String updatedCity = cityTf.getText();

                try {
                    int age = Integer.parseInt(updatedAge);


                    try {
                        conn = DBConnection.getConnection();
                        String sql = "UPDATE CLIENTS SET CLIENT_NAME = ?, CLIENT_AGE = ?, CLIENT_GENDER = ?, CLIENT_CITY = ? WHERE CLIENT_ID = ?";
                        state = conn.prepareStatement(sql);

                        state.setString(1, updatedName);
                        state.setInt(2, age);
                        state.setString(3, updatedGender);
                        state.setString(4, updatedCity);
                        state.setInt(5, clientId);

                        int rowsAffected = state.executeUpdate();
                        if (rowsAffected > 0) {
                            refreshTable();
                            refreshComboPerson();
                            clearForm();
                        } else {
                            JOptionPane.showMessageDialog(null, "Не можа да се извърши актуализацията.");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Грешка при актуализиране на данните.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Моля, въведете валидна възраст.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Изберете клиент за редактиране.");
            }
        }
    }

    class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                conn = DBConnection.getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String sql = "SELECT * FROM CLIENTS WHERE CLIENT_AGE = ?";

            try {
                state = conn.prepareStatement(sql);
                state.setInt(1, Integer.parseInt(ageTf.getText()));
                result = state.executeQuery();
                table.setModel(new MyModel(result));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class RefreshAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                refreshTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
