package WidokAdmin;

import DBHandler.DBHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by Magdalena on 2017-01-25.
 */
public class PracownikWidok implements ActionListener{
    private JPanel panel1;
    private WidokAdmin jFrame;
    private JTextField imieField;
    private JTextField nazwiskoField;
    private JTextField dataUrodField;
    private JTextField numerKontaField;
    private JTextField peselField;
    private JTextField zatrOdField;
    private JTextField zatrDoField;
    private JButton zapiszBtn;
    private JButton cofnijBtn;
    private JRadioButton KOBIETARadioButton;
    private JRadioButton MEZCZYZNARadioButton;
    int id = 0;

    PracownikWidok (WidokAdmin frame){
        zapiszBtn.addActionListener(this);
        cofnijBtn.addActionListener(this);
        jFrame = frame;
    }

    PracownikWidok (WidokAdmin frame, int id, String im, String naz, String pes, char p, Date data, Date zOd, Date zDo, String nr){
        imieField.setText(im);
        nazwiskoField.setText(naz);
        peselField.setText(pes);
        if (p == 'M'){
            MEZCZYZNARadioButton.setSelected(true);
        }else{
            KOBIETARadioButton.setSelected(true);
        }
        dataUrodField.setText(data.toString());
        zatrOdField.setText(zOd.toString());
        zatrDoField.setText(zDo.toString());
        numerKontaField.setText(String.valueOf(nr));
        zapiszBtn.addActionListener(this);
        cofnijBtn.addActionListener(this);
        this.id = id;
        jFrame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("ZAPISZ")){
            try {
                DBHandler dbHandler = DBHandler.wezInstancje();
                Connection connection = dbHandler.getDeskryptorPolaczenia();
                if (id!=0) {
                    String updatePracownikSQL = "UPDATE PRACOWNICY SET IMIE = ?, NAZWISKO = ?, DATA_URODZENIA =?, PLEC = ?, NR_KONTA = ?, ZATRUDNIONY_OD = ?, ZATRUDNIONY_DO = ?, PESEL = ? WHERE PRACOWNIK_ID = ?";
                    PreparedStatement stmt = connection.prepareStatement(updatePracownikSQL);
                    stmt.setString(1, imieField.getText());
                    stmt.setString(2, nazwiskoField.getText());
                    stmt.setDate(3, getDateFromString(dataUrodField.getText()));
                    stmt.setString(4, MEZCZYZNARadioButton.isSelected()? "M": "K");
                    stmt.setString(5, numerKontaField.getText());
                    stmt.setDate(6, getDateFromString(zatrOdField.getText()));
                    stmt.setDate(7, getDateFromString(zatrDoField.getText()));
                    stmt.setString(8, peselField.getText());
                    stmt.setInt(9, id);
                    stmt.executeUpdate();
                    stmt.execute("COMMIT");
                    stmt.close();
                }else {
                    String updatePracownikSQL = "INSERT INTO PRACOWNICY (ID, IMIE, NAZWISKO, DATA_URODZENIA, PLEC, NR_KONTA, ZATRUDNIONY_OD, ZATRUDNIONY_DO, PESEL, FILHARMONIA_ID, ADRES_ID) = (?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(updatePracownikSQL);
                    stmt.setString(1, imieField.getText());
                    stmt.setString(2, nazwiskoField.getText());
                    stmt.setDate(3, getDateFromString(dataUrodField.getText()));
                    stmt.setString(4, MEZCZYZNARadioButton.isSelected()? "M": "K");
                    stmt.setString(5, numerKontaField.getText());
                    stmt.setDate(6, getDateFromString(zatrOdField.getText()));
                    stmt.setDate(7, getDateFromString(zatrDoField.getText()));
                    stmt.setString(8, peselField.getText());
                    stmt.setInt(9, id);
                    stmt.executeUpdate(updatePracownikSQL);
                    stmt.execute("COMMIT");
                    stmt.close();

                }
                jFrame.getWszyscyPracownicy();
                jFrame.wyswietl();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }else{
            try {
                jFrame.getWszyscyPracownicy();
                jFrame.wyswietl();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private Date getDateFromString(String text) {
        int year = Integer.parseInt(text.substring(0,4));
        int month = Integer.parseInt(text.substring(5,7));
        int day = Integer.parseInt(text.substring(8,10));
        System.out.println(year + "," + month + "," + day);
        return new Date(year,month,day);
    }

    public JPanel getPanel1(){
        return panel1;
    }
}
