import javax.swing.*;

public class TappedPanes extends JFrame {
    JPanel personP = new JPanel();
    JPanel carP = new JPanel();
    JPanel rentalP = new JPanel();


    JTabbedPane tabbedPane = new JTabbedPane();

    public TappedPanes(){
        this.setSize(400, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane.add(personP , "Коли");
        tabbedPane.add(carP , "Клиенти");
        tabbedPane.add(rentalP , "Продажби");
        this.add(tabbedPane);
        this.setVisible(true);
    }
}
