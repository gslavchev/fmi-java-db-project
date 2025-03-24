package frame.tablespanels;

import database.access.DBConnection;
import database.tablemodel.MyModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    JComboBox<String> customerCb = new JComboBox<>();
    HashMap<Integer, String> customerMap = new HashMap<>();
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
        upPanel.add(customerCb);
        upPanel.add(sellDateLb);
        upPanel.add(sellDateTf);

        midPanel.add(addButton);
        midPanel.add(deleteButton);
        midPanel.add(editButton);
        midPanel.add(searchButton);
        midPanel.add(refreshButton);

        myScroll.setPreferredSize(new Dimension(350, 150));
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
        loadCustomers();
        clearForm();
        refreshTable();

        table.addMouseListener(new MouseAction(this));

        this.setVisible(true);
    }

    private void loadCars() {
        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT CAR_ID, CAR_MODEL FROM CARS WHERE CAR_STATUS = 'available';";
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

    private void loadCustomers() {
        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT CLIENT_ID, CLIENT_NAME FROM CLIENTS";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            customerCb.removeAllItems();
            customerMap.clear();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("CLIENT_ID");
                String customerName = resultSet.getString("CLIENT_NAME");

                customerCb.addItem(customerName);
                customerMap.put(customerId, customerName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearForm() {
        if (carCb.getItemCount() > 0) {
            carCb.setSelectedIndex(0);
        }
        if (customerCb.getItemCount() > 0) {
            customerCb.setSelectedIndex(0);
        }
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

    private int getSelectedCustomerId() {
        String selectedCustomer = (String) customerCb.getSelectedItem();

        for (Integer customerId : customerMap.keySet()) {
            if (customerMap.get(customerId).equals(selectedCustomer)) {
                return customerId;
            }
        }
        return -1;
    }

    public class MouseAction implements MouseListener {

        private SalesPanel salesPanel;

        public MouseAction(SalesPanel salesPanel) {
            this.salesPanel = salesPanel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = salesPanel.table.rowAtPoint(e.getPoint());
            if (row >= 0) {
                int salesId = (int) salesPanel.table.getValueAt(row, 0);
                String carModel = (String) salesPanel.table.getValueAt(row, 1);
                String customerName = (String) salesPanel.table.getValueAt(row, 2);

                Object sellDateObj = salesPanel.table.getValueAt(row, 3);
                String sellDate = "";

                if (sellDateObj instanceof Date) {
                    java.sql.Date sqlDate = (java.sql.Date) sellDateObj;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    sellDate = sdf.format(sqlDate);
                }

                salesPanel.id = salesId;
                salesPanel.carCb.setSelectedItem(carModel);
                salesPanel.customerCb.setSelectedItem(customerName);
                salesPanel.sellDateTf.setText(sellDate);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


    class AddAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
                String sql = "INSERT INTO SALES (CAR_ID, CLIENT_ID, BUYING_DATE) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(sql);

                idCar = getSelectedCarId();
                idCustomer = getSelectedCustomerId();

                statement.setInt(1, idCar);
                statement.setInt(2, idCustomer);

                String sellDate = sellDateTf.getText().trim();

                if (sellDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Моля, въведете валидна дата.");
                    return;
                }

                if (!isValidDate(sellDate)) {
                    JOptionPane.showMessageDialog(null, "Невалиден формат на датата. Моля, използвайте DD-MM-YYYY.");
                    return;
                }

                try {
                    LocalDate date = LocalDate.parse(sellDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    statement.setDate(3, Date.valueOf(date));
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Невалидна дата. Моля, въведете валидна дата.");
                    return;
                }

                statement.execute();

                sql = "UPDATE CARS SET CAR_STATUS = 'SOLD' WHERE CAR_ID = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, idCar);
                statement.executeUpdate();

                refreshTable();
                loadCars();
                loadCustomers();
                clearForm();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        private boolean isValidDate(String date) {
            try {
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                return true;
            } catch (DateTimeParseException ex) {
                return false;
            }
        }
    }
    private void refreshTable() {
        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT SALES.SALES_ID, CARS.CAR_MODEL, CLIENTS.CLIENT_NAME ,CARS.PRICE , SALES.BUYING_DATE " +
                    "FROM SALES " +
                    "JOIN CARS ON SALES.CAR_ID = CARS.CAR_ID " +
                    "JOIN CLIENTS ON SALES.CLIENT_ID = CLIENTS.CLIENT_ID";
            statement = connection.prepareStatement(sql);
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
                String sql = "DELETE FROM SALES WHERE SALES_ID = ?";
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
                String sql = "UPDATE SALES SET CAR_ID = ?, CLIENT_ID = ?, BUYING_DATE = ? WHERE SALES_ID = ?";
                statement = connection.prepareStatement(sql);

                statement.setInt(1, getSelectedCarId());
                statement.setInt(2, getSelectedCustomerId());

                String sellDate = sellDateTf.getText().trim();

                if (sellDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Моля, въведете валидна дата.");
                    return;
                }

                if (!isValidDate(sellDate)) {
                    JOptionPane.showMessageDialog(null, "Невалиден формат на датата. Моля, използвайте DD-MM-YYYY.");
                    return;
                }

                try {
                    LocalDate date = LocalDate.parse(sellDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    statement.setDate(3, Date.valueOf(date));
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Невалидна дата. Моля, въведете валидна дата.");
                    return;
                }

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
                String searchDate = sellDateTf.getText().trim();
                String customerName = customerCb.getSelectedItem().toString().trim();
                connection = DBConnection.getConnection();

                /*if (searchDate.isEmpty() && !customerName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Търсене по клиент");
                    String sql = "SELECT SALES.SALES_ID, CARS.CAR_MODEL, CLIENTS.CLIENT_NAME, SALES.BUYING_DATE " +
                            "FROM SALES " +
                            "JOIN CARS ON SALES.CAR_ID = CARS.CAR_ID " +
                            "JOIN CLIENTS ON SALES.CLIENT_ID = CLIENTS.CLIENT_ID WHERE CLIENTS.CLIENT_NAME = ?";

                    statement = connection.prepareStatement(sql);
                    statement.setString(1, customerName);
                    resultSet = statement.executeQuery();

                    table.setModel(new MyModel(resultSet));

                    return;
                }
                if (!isValidDate(searchDate)) {
                    JOptionPane.showMessageDialog(null, "Невалиден формат на датата. Моля, използвайте DD-MM-YYYY.");
                    return;
                }

                LocalDate date = LocalDate.parse(searchDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                String sql = "SELECT SALES.SALES_ID, CARS.CAR_MODEL, CLIENTS.CLIENT_NAME, SALES.BUYING_DATE " +
                        "FROM SALES " +
                        "JOIN CARS ON SALES.CAR_ID = CARS.CAR_ID " +
                        "JOIN CLIENTS ON SALES.CLIENT_ID = CLIENTS.CLIENT_ID WHERE SALES.BUYING_DATE = ?";
                statement = connection.prepareStatement(sql);
                statement.setDate(1, Date.valueOf(date));
                resultSet = statement.executeQuery();

                table.setModel(new MyModel(resultSet)); */

                  if (searchDate.isEmpty() && customerName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Изберете критерии за търсене!");
                    return;
                }
                if (!isValidDate(searchDate)) {
                    JOptionPane.showMessageDialog(null, "Невалиден формат на датата. Моля, използвайте DD-MM-YYYY.");
                    return;
                }

                LocalDate date = LocalDate.parse(searchDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                String sql = "SELECT SALES.SALES_ID, CARS.CAR_MODEL, CLIENTS.CLIENT_NAME, SALES.BUYING_DATE " +
                        "FROM SALES " +
                        "JOIN CARS ON SALES.CAR_ID = CARS.CAR_ID " +
                        "JOIN CLIENTS ON SALES.CLIENT_ID = CLIENTS.CLIENT_ID WHERE SALES.BUYING_DATE = ? AND CLIENTS.CLIENT_NAME = ?";
                statement = connection.prepareStatement(sql);
                statement.setDate(1, Date.valueOf(date));
                statement.setString(2, customerName);
                resultSet = statement.executeQuery();

                table.setModel(new MyModel(resultSet));

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Невалидна дата. Моля, въведете валидна дата.");
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
            loadCustomers();
            clearForm();
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate enterDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            if (enterDate.isAfter(LocalDate.now())) {
                return false;
            }
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
