package WidokAdmin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Magdalena on 2017-01-26.
 */
public class SzukajWidok implements ActionListener{
    private WidokAdmin jFrame;
    private JTextField imieTextField;
    private JPanel panel1;
    private JTextField nazwiskoTextField;
    private JTextField peselTextField;
    private JTextField filharmoniaTextField;
    private JButton szukajButton;

    public SzukajWidok(WidokAdmin jframe){
        jFrame = jframe;
        szukajButton.addActionListener(this);
    }
    public JPanel getPanel1(){
        return panel1;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            jFrame.filtrujPracownikow(imieTextField.getText(),nazwiskoTextField.getText(), peselTextField.getText(), filharmoniaTextField.getText() );
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        jFrame.wyswietl();
    }
}
