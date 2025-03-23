package frame.tablespanels;

import database.access.DBConnection;
import database.tablemodel.MyModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

public class SalesPanel extends JPanel {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    int id = -1;
    int idCar = -1;
    int idCustomer = -1;

    JLabel carLb = new JLabel("Кола: ");
    JComboBox<String> carCb = new JComboBox<>();
    HashMap<Integer, String> carMap = new HashMap<>();
    JLabel customerLb = new JLabel("Клиент: ");

    JLabel sellDateLb = new JLabel("Дата:");
    JTextField sellDateTf = new JTextField();

    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton editButton = new JButton("Edit");
    JButton searchButton = new JButton("Search");
    JButton refreshButton = new JButton("Refresh");

    JTable table = new JTable();
    JScrollPane myScroll = new JScrollPane(table);

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();
    public SalesPanel() {
        this.setSize(400, 500);
        this.setLayout(new GridLayout(3, 1));


        upPanel.setLayout(new GridLayout(3, 2));
        upPanel.add(carLb);
        upPanel.add(carCb);
        upPanel.add(customerLb);
        upPanel.add(sellDateLb);
        upPanel.add(sellDateTf);


        midPanel.add(addButton);
        midPanel.add(deleteButton);
        midPanel.add(editButton);
        midPanel.add(searchButton);
        midPanel.add(refreshButton);

        myScroll.setPreferredSize(new Dimension(350 , 150));
        downPanel.add(myScroll);

        this.add(upPanel);
        this.add(midPanel);
        this.add(downPanel);

        addButton.addActionListener(new AddAction());
        deleteButton.addActionListener(new DeleteAction());
        editButton.addActionListener(new EditAction());
        searchButton.addActionListener(new SearchAction());
        refreshButton.addActionListener(new RefreshAction());

        loadCars();
        clearForm();
        refreshTable();
        
        this.setVisible(true);
    }

    private void loadCars() {
        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT CAR_ID, CAR_MODEL FROM CARS WHERE CAR_STATUS IS NULL";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            carCb.removeAllItems();
            carMap.clear();

            while (resultSet.next()) {
                int carId = resultSet.getInt("CAR_ID");
                String carName = resultSet.getString("CAR_MODEL");

                carCb.addItem(carName);
                carMap.put(carId, carName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearForm() {
        carCb.setSelectedIndex(0);
        sellDateTf.setText("");
        id = 1;
    }

    private int getSelectedCarId() {
        String selectedCar = (String) carCb.getSelectedItem();

        for (Integer carId : carMap.keySet()) {
            if (carMap.get(carId).equals(selectedCar)) {
                return carId;
            }
        }
        return -1;
    }

    class AddAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
                String sql = "INSERT INTO SALES (CAR_ID, CUSTOMER_NAME, SELL_DATE) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(sql);

                idCar = getSelectedCarId();
                statement.setInt(1,idCar);
                //statement.setString(2, customerTf.getText());
                statement.setDate(3, Date.valueOf(sellDateTf.getText()));

                statement.execute();

                sql = "UPDATE CARS SET CAR_STATUS = 'SOLD' WHERE CAR_ID = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, idCar);
                statement.executeUpdate();

                refreshTable();
                loadCars();
                clearForm();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void refreshTable() {
        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM SALES;");
            resultSet = statement.executeQuery();
            table.setModel(new MyModel(resultSet));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (id == -1) return;

            try {
                connection = DBConnection.getConnection();
                String sql = "DELETE FROM SALES WHERE SALE_ID = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, id);
                statement.execute();

                refreshTable();
                clearForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    class EditAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (id == -1) return;

            try {
                connection = DBConnection.getConnection();
                String sql = "UPDATE SALES SET CAR_ID = ?, CUSTOMER_NAME = ?, SELL_DATE = ? WHERE SALE_ID = ?";
                statement = connection.prepareStatement(sql);

                statement.setInt(1, getSelectedCarId());
                //statement.setString(2, customerTf.getText());
                statement.setDate(3, Date.valueOf(sellDateTf.getText()));
                statement.setInt(4, id);

                statement.execute();
                refreshTable();
                clearForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
                String sql = "SELECT * FROM SALES WHERE CUSTOMER_NAME = ?";
                statement = connection.prepareStatement(sql);
                //statement.setString(1, customerTf.getText());
                resultSet = statement.executeQuery();
                table.setModel(new MyModel(resultSet));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class RefreshAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshTable();
            loadCars();
            clearForm();
        }
    }
}
