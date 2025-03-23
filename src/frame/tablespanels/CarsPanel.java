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
import java.time.LocalDate;

public class CarsPanel extends JPanel {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    int id = 1;
    
    
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

    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton editButton = new JButton("Edit");
    JButton searchButton = new JButton("Search");
    JButton refreshButton = new JButton("Refresh");


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

        midPanel.add(addButton);
        midPanel.add(deleteButton);
        midPanel.add(editButton);
        midPanel.add(searchButton);
        midPanel.add(refreshButton);
        this.add(midPanel);

        myScroll.setPreferredSize(new Dimension(350 , 150));
        downPanel.add(myScroll);
        this.add(downPanel);
        refreshTable();
        
        addButton.addActionListener(new AddAction());
        table.addMouseListener(new MouseAction());
        deleteButton.addActionListener(new DeleteAction());
        searchButton.addActionListener(new SearchAction());
        refreshButton.addActionListener(new RefreshAction());
        editButton.addActionListener(new EditAction());
        
        this.setVisible(true);
    }

    public void refreshTable(){

        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM CARS WHERE CAR_STATUS = 'available';");
            resultSet = statement.executeQuery();
            table.setModel(new MyModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void clearForm(){
        carModelTf.setText("");
        yearOfProductionTf.setText("");
        horsePowerTf.setText("");
        mileageTf.setText("");
        priceTf.setText("");
        gearboxCb.setSelectedIndex(0);       
        fuelCb.setSelectedIndex(0);       
    }

    class AddAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String sql="insert into CARS (CAR_MODEL, YEAR_OF_PRODUCTION , HORSE_POWER ," +
                    " GEARBOX, FUEL, MILEAGE, PRICE, CAR_STATUS) " +
                    "values (?,?,?,?,?,?,?,?)";
            try {
                statement=connection.prepareStatement(sql);
                statement.setString(1, carModelTf.getText());
                if(Integer.parseInt(yearOfProductionTf.getText()) > LocalDate.now().getYear()){
                    JOptionPane.showMessageDialog(null, "Невалидна дата на производство!",
                            "Грешни данни!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                statement.setInt(2, Integer.parseInt(yearOfProductionTf.getText()));
                statement.setInt(3, Integer.parseInt(horsePowerTf.getText()));
                statement.setString(4, gearboxCb.getSelectedItem().toString());
                statement.setString(5, fuelCb.getSelectedItem().toString());
                statement.setInt(6, Integer.parseInt(mileageTf.getText()));
                statement.setDouble(7, Double.parseDouble(priceTf.getText()));
                statement.setString(8, "available");
                statement.execute();
                refreshTable();
                clearForm();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Проверете вашите входни данни!",
                        "Грешни данни!", JOptionPane.WARNING_MESSAGE);
                throw new RuntimeException(ex);
            }
        }
    }

    public class MouseAction implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            id = Integer.parseInt(table.getValueAt(row , 0).toString());
            carModelTf.setText(table.getValueAt(row , 1).toString());
            yearOfProductionTf.setText(table.getValueAt(row , 2).toString());
            horsePowerTf.setText(table.getValueAt(row , 3).toString());
            mileageTf.setText(table.getValueAt(row , 6).toString());
            priceTf.setText(table.getValueAt(row , 7).toString());
            if(table.getValueAt(row , 4).toString().equals("Автоматични")){
                gearboxCb.setSelectedIndex(0);
            } else {
                gearboxCb.setSelectedIndex(1);
            }
            if(table.getValueAt(row , 5).toString().equals("Бензин")){
                fuelCb.setSelectedIndex(0);
            } else {
                fuelCb.setSelectedIndex(1);
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

    public class DeleteAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
                String sql = "DELETE FROM CARS WHERE CAR_ID = ?";
                statement=connection.prepareStatement(sql);
                statement.setInt(1 ,id);
                statement.execute();
                refreshTable();
                clearForm();
                id = -1;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public class SearchAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                connection = DBConnection.getConnection();
                String sql = "select * from CARS where CAR_MODEL = ? AND CAR_STATUS = 'available';";
                statement = connection.prepareStatement(sql);
                statement.setString(1 , carModelTf.getText());
                resultSet = statement.executeQuery();
                table.setModel(new MyModel(resultSet));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public class RefreshAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            refreshTable();
            clearForm();
        }
    }

    public class EditAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DBConnection.getConnection();
                String sql = "UPDATE CARS " +
                        "SET CAR_MODEL = ?, YEAR_OF_PRODUCTION = ?, HORSE_POWER = ?, " +
                        "GEARBOX = ?, FUEL = ?, MILEAGE = ?, PRICE = ? " +
                        "WHERE CAR_ID = ?;";

                statement=connection.prepareStatement(sql);
                statement.setString(1, carModelTf.getText());
                statement.setInt(2, Integer.parseInt(yearOfProductionTf.getText()));
                statement.setInt(3, Integer.parseInt(horsePowerTf.getText()));
                statement.setString(4, gearboxCb.getSelectedItem().toString());
                statement.setString(5, fuelCb.getSelectedItem().toString());
                statement.setInt(6, Integer.parseInt(mileageTf.getText()));
                statement.setDouble(7, Double.parseDouble(priceTf.getText()));
                statement.setInt(8 ,id);
                statement.execute();
                
                refreshTable();
                clearForm();
                id = -1;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
