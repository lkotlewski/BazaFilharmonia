package WidokAdmin;

import DBHandler.DBHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    private JTextField ulicaField;
    private JTextField miastoField;
    private JTextField nrDomuField;
    private JTextField nrMieszField;
    int id = 0;
    int adres_id = 0;

    PracownikWidok (WidokAdmin frame){
        zapiszBtn.addActionListener(this);
        cofnijBtn.addActionListener(this);
        jFrame = frame;
    }

    PracownikWidok (WidokAdmin frame, int id, String im, String naz, String pes, char p, Date data, Date zOd, Date zDo, String nr, String ulica, String nrBud, int nrMiesz, String miasto, int addID){
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
        ulicaField.setText(ulica);
        miastoField.setText(miasto);
        nrDomuField.setText(nrBud);
        nrMieszField.setText(String.valueOf(nrMiesz));

        zapiszBtn.addActionListener(this);
        cofnijBtn.addActionListener(this);
        this.id = id;
        adres_id = addID;
        jFrame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        Connection connection = null;
        if (command.equalsIgnoreCase("ZAPISZ")){
            try {
                DBHandler dbHandler = DBHandler.wezInstancje();
                connection = dbHandler.getDeskryptorPolaczenia();
                if (id!=0) {
                    connection.setAutoCommit(false);
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
                    connection.commit();
                    stmt.close();
                    String updateAdresSQL = "UPDATE ADRESY SET ULICA = ?, NR_BUDYNKU = ?, NR_MIESZKANIA =?, MIASTO = ? WHERE ADRES_ID = ?";
                    stmt = connection.prepareStatement(updateAdresSQL);
                    stmt.setString(1, ulicaField.getText());
                    stmt.setString(2, nrDomuField.getText());
                    stmt.setInt(3, Integer.parseInt(nrMieszField.getText()));
                    stmt.setString(4, miastoField.getText());
                    stmt.setInt(5, adres_id);
                    stmt.executeUpdate();
                    connection.commit();
                    stmt.close();
                }else {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT MAX(ADRES_ID) from ADRESY");

                    int adres_id = 0;
                    while (rs.next()){
                        adres_id = rs.getInt(1) + 1;
                    }
                    rs.close();
                    statement.close();
                    String createAdresSQL = "INSERT INTO ADRESY (ADRES_ID, ULICA, NR_BUDYNKU, NR_MIESZKANIA, MIASTO) VALUES (?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(createAdresSQL);
                    preparedStatement.setInt(1, adres_id);
                    preparedStatement.setString(2, ulicaField.getText());
                    preparedStatement.setString(3, nrDomuField.getText());
                    preparedStatement.setInt(4, Integer.parseInt(nrMieszField.getText()));
                    preparedStatement.setString(5, miastoField.getText());
                    preparedStatement.executeQuery();
                    connection.commit();
                    statement = connection.createStatement();
                    rs = statement.executeQuery("SELECT MAX(PRACOWNIK_ID) from PRACOWNICY");
                    int id = 0;
                    while (rs.next()){
                        id = rs.getInt(1) + 1;
                    }
                    String updatePracownikSQL = "INSERT INTO PRACOWNICY (PRACOWNIK_ID, IMIE, NAZWISKO, DATA_URODZENIA, PLEC, NR_KONTA, ZATRUDNIONY_OD, ZATRUDNIONY_DO, PESEL, FILHARMONIA_ID, ADRES_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(updatePracownikSQL);
                    stmt.setInt(1, id);
                    stmt.setString(2, imieField.getText());
                    stmt.setString(3, nazwiskoField.getText());
                    stmt.setDate(4, getDateFromString(dataUrodField.getText()));
                    stmt.setString(5, MEZCZYZNARadioButton.isSelected()? "M": "K");
                    stmt.setString(6, numerKontaField.getText());
                    stmt.setDate(7, getDateFromString(zatrOdField.getText()));
                    stmt.setDate(8, getDateFromString(zatrDoField.getText()));
                    stmt.setString(9, peselField.getText());
                    stmt.setInt(10, 1);
                    stmt.setInt(11, adres_id);
                    stmt.executeQuery();
                    connection.commit();
                    stmt.close();

                }
                jFrame.getWszyscyPracownicy();
                jFrame.wyswietl();
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Niepoprawny formularz");
                try {
                    connection.rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                e1.printStackTrace();
            } catch (Exception e1){
                JOptionPane.showMessageDialog(null, "Niepoprawny formularz");
                try {
                    connection.rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                e1.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

    private Date getDateFromString(String text) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(simpleDateFormat.parse(text).getTime());
    }

    public JPanel getPanel1(){
        return panel1;
    }
}
