package WidokKsiegowy;

import DBHandler.DBHandler;
import WidokAdmin.WidokAdmin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Tomek on 2017-01-27.
 */
public class WidokKsiegowy implements ActionListener {
    private JButton wynagrodzeniaButton;
    private JPanel panel1;
    private JButton dodajWynagrodzenieButton;
    private JPanel kontentPanel;
    private JFrame frame;
    private DBHandler dbHandler;
    private JScrollPane scrollPane;
    private JTable tabelaPracownikow;
    private static final String[] columnNames = {
            "IMIE",
            "NAZWISKO",
            "PESEL",
            "KWOTA BRUTTO",
            "DATA WYPLATY"};

    public WidokKsiegowy(){

        dodajWynagrodzenieButton.addActionListener(this);
        wynagrodzeniaButton.addActionListener(this);
        try {
            getWynagrodzenia();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getWynagrodzenia() throws SQLException{
        dbHandler = DBHandler.wezInstancje();
        Connection connection = dbHandler.getDeskryptorPolaczenia();

        Statement stmt = connection.createStatement();
        String getPracownicyCount = "SELECT COUNT(*) from WYNAGRODZENIE";
        String getPracownicySQL = "SELECT w.KWOTA_BRUTTO, w.DATA_WYPLATY, p.IMIE, p.NAZWISKO, p.PESEL from WYNAGRODZENIE w INNER JOIN PRACOWNICY p ON w.PRACOWNIK_ID = p.PRACOWNIK_ID";
        ResultSet countRS = stmt.executeQuery(getPracownicyCount);
        int count = 0;
        if (countRS.next()){
            count = countRS.getInt(1);
        }
        countRS.close();
        stmt.close();
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(getPracownicySQL);
        Object [][] data = new Object[count][5];
        int index = 0;
        while(rs.next()){
            data[index][3] = (Object) rs.getFloat(1);
            data[index][4] = (Object) rs.getDate(2);
            data[index][0] = (Object) rs.getString(3);
            data[index][1] = (Object) rs.getString(4);
            data[index][2] = (Object) rs.getString(5);
            index++;
        }
        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(data, columnNames);
        tabelaPracownikow = new JTable(dm);
        tabelaPracownikow.setVisible(true);
        scrollPane = new JScrollPane(tabelaPracownikow);
        tabelaPracownikow.setFillsViewportHeight(true);
        kontentPanel.removeAll();
        kontentPanel.add(scrollPane);
        panel1.repaint();
        rs.close();
        stmt.close();
    }

    public void wyswietl() {
        if (frame == null) {
            frame = new JFrame("Panel ksiegowej");
        }
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Dodaj wynagrodzenie")) {
            dodajWynagrodzenie();
        }else if (command.equalsIgnoreCase("Wynagrodzenia")) {
            try {
                getWynagrodzenia();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        wyswietl();

    }

    private void dodajWynagrodzenie() {
        frame.getContentPane().remove(kontentPanel);
        kontentPanel.removeAll();
        WynagrodzenieWidok widok = new WynagrodzenieWidok(this);
        kontentPanel.add(widok.getPanel1());
        kontentPanel.setVisible(true);
        panel1.repaint();
        frame.getContentPane().add(kontentPanel);
        frame.revalidate();
    }
}
