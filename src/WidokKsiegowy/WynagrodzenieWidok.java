package WidokKsiegowy;

import DBHandler.DBHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Tomek on 2017-01-27.
 */
public class WynagrodzenieWidok implements ActionListener{
    private JPanel panel;
    private JTextField imieField1;
    private JTextField nazwiskoField;
    private JTextField peselField;
    private JTextField kwotaField;
    private JTextField dataField;
    private JButton dodajButton;
    private JButton cofnijButton;
    private WidokKsiegowy jFrame;

    public WynagrodzenieWidok(WidokKsiegowy frame) {
        dodajButton.addActionListener(this);
        cofnijButton.addActionListener(this);
        this.jFrame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Dodaj")) {
            zapiszWynagrodzenie();
            try {
                jFrame.getWynagrodzenia();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            jFrame.wyswietl();
        }else {
            try {
                jFrame.getWynagrodzenia();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            jFrame.wyswietl();
        }

    }

    private void zapiszWynagrodzenie() {
        Connection connection = null;
        int pracownik_id =0;
        try {
            DBHandler dbHandler = DBHandler.wezInstancje();
            connection = dbHandler.getDeskryptorPolaczenia();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(WYNAGRODZENIE_ID) from WYNAGRODZENIE");
            int id = 0;
            while (rs.next()){
                id = rs.getInt(1) + 1;
            }
            rs.close();
            statement.close();
            String countsql = "SELECT count(*) from PRACOWNICY p WHERE p.IMIE LIKE ? AND p.NAZWISKO LIKE ? AND p.PESEL LIKE ?";
            PreparedStatement count_stmt = connection.prepareStatement(countsql);
            count_stmt.setString(1, imieField1.getText() +"%");
            count_stmt.setString(2, nazwiskoField.getText()+"%");
            count_stmt.setString(3, peselField.getText()+"%");
            int count = 0;
            ResultSet countrs = count_stmt.executeQuery();
            while(countrs.next()){
                count = countrs.getInt(1);
            }
            if (count!=1){
                throw new Exception();
            }
            countrs.close();
            count_stmt.close();
            String sql = "SELECT p.PRACOWNIK_ID from PRACOWNICY p WHERE p.IMIE LIKE ? AND p.NAZWISKO LIKE ? AND p.PESEL LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, imieField1.getText() +"%");
            stmt.setString(2, nazwiskoField.getText()+"%");
            stmt.setString(3, peselField.getText()+"%");
            ResultSet rs1 = stmt.executeQuery();
            while (rs1.next()){
                pracownik_id = rs1.getInt(1);
            }
            rs1.close();
            stmt.close();
            String updatePracownikSQL = "INSERT INTO WYNAGRODZENIE (WYNAGRODZENIE_ID, KWOTA_BRUTTO, DATA_WYPLATY, PRACOWNIK_ID) VALUES (?,?,?,?)";
            PreparedStatement stmt1 = connection.prepareStatement(updatePracownikSQL);
            stmt1.setInt(1, id);
            stmt1.setFloat(2, Float.parseFloat((kwotaField.getText()).replace(',','.')));
            stmt1.setDate(3, getDateFromString(dataField.getText()));
            stmt1.setInt(4, pracownik_id);
            stmt1.executeQuery();
            connection.commit();
            stmt.close();
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Niepoprawny formularz");
            try {
                connection.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e1.printStackTrace();
        } catch (Exception e1) {
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
    }

    private Date getDateFromString(String text) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(simpleDateFormat.parse(text).getTime());
    }

    public JPanel getPanel1() {
        return panel;
    }
}
