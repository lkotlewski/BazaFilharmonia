package WidokAdmin;

import DBHandler.DBHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Magdalena on 2017-01-24.
 */
public class WidokAdmin implements ActionListener{

    private JFrame frame;
    private JPanel panelGlownyAdmin;
    private JButton pracownicyBtn;
    private JButton dodajPracownikaBtn;
    private JButton znajdzPracownikaBtn;
    private JPanel kontentPanel;
    private JTable pracownicyTable;
    private DBHandler dbHandler;
    private JScrollPane scrollPane;
    private JTable tabelaPracownikow;
    private static final String[] columnNames = {
            "IMIE",
            "NAZWISKO",
            "PESEL",
            "ZATRUDNIONY OD",
            "ZATRUDNIONY DO",
            "FILHARMONIA", "EDYCJA"};

    private void createUIComponents() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("WSZYSCY PRACOWNICY")) {
            try {
                getWszyscyPracownicy();
                wyswietl();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }else if (command.equalsIgnoreCase("ZNAJDZ PRACOWNIKA")){
            znajdzPracownika();
            wyswietl();
        }else{
            dodajPracownika();
            wyswietl();
        }
    }

    public WidokAdmin(){
        pracownicyBtn.addActionListener(this);
        dodajPracownikaBtn.addActionListener(this);
        znajdzPracownikaBtn.addActionListener(this);
        try {
            getWszyscyPracownicy();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void wyswietl() {
        if (frame == null) {
            frame = new JFrame("Panel administratora");
        }
        frame.setContentPane(this.panelGlownyAdmin);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void getWszyscyPracownicy() throws SQLException {
        dbHandler = DBHandler.wezInstancje();
        Connection connection = dbHandler.getDeskryptorPolaczenia();
        Statement stmt = connection.createStatement();
        String getPracownicyCount = "SELECT COUNT(*) from PRACOWNICY";
        String getPracownicySQL = "SELECT p.PRACOWNIK_ID, p.IMIE, p.NAZWISKO, p.PESEL, p.ZATRUDNIONY_OD, p.ZATRUDNIONY_DO, f.NAZWA_OFICJALNA from PRACOWNICY p INNER JOIN FILHARMONIE f on f.FILHARMONIA_ID = p.FILHARMONIA_ID";
        ResultSet countRS = stmt.executeQuery(getPracownicyCount);
        int count = 0;
        if (countRS.next()){
            count = countRS.getInt(1);
        }
        countRS.close();
        stmt.close();
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(getPracownicySQL);
        pracownicyTable = new JTable();
        Object [][] data = new Object[count][7];
        int index = 0;
        while(rs.next()){
            data[index][6] = (Object) rs.getInt(1);
            data[index][0] = (Object) rs.getString(2);
            data[index][1] = (Object) rs.getString(3);
            data[index][2] = (Object) rs.getString(4);
            data[index][3] = (Object) rs.getDate(5);
            data[index][4] = (Object) rs.getDate(6);
            data[index][5] = (Object) rs.getString(7);
            index++;
        }
        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(data, columnNames);
        tabelaPracownikow = new JTable(dm);
        tabelaPracownikow.setVisible(true);
        tabelaPracownikow.getColumn("EDYCJA").setCellRenderer(new ButtonRenderer());
        scrollPane = new JScrollPane(tabelaPracownikow);
        tabelaPracownikow.setFillsViewportHeight(true);
        kontentPanel.removeAll();
        kontentPanel.add(scrollPane);
        panelGlownyAdmin.repaint();
        rs.close();
        stmt.close();
    }

    public void filtrujPracownikow(String imie, String nazwisko, String pesel, String filharmonia) throws SQLException {
        dbHandler = DBHandler.wezInstancje();
        Connection connection = dbHandler.getDeskryptorPolaczenia();
        String countsql = "SELECT count(*) from PRACOWNICY p INNER JOIN FILHARMONIE f on f.FILHARMONIA_ID = p.FILHARMONIA_ID"
                + " WHERE p.IMIE LIKE ? AND p.NAZWISKO LIKE ? AND p.PESEL LIKE ? AND f.NAZWA_OFICJALNA LIKE ?";
        PreparedStatement count_stmt = connection.prepareStatement(countsql);
        count_stmt.setString(1, imie +"%");
        count_stmt.setString(2, nazwisko+"%");
        count_stmt.setString(3, pesel+"%");
        count_stmt.setString(4, filharmonia+"%");
        int count = 0;
        ResultSet countrs = count_stmt.executeQuery();
        while(countrs.next()){
            count = countrs.getInt(1);
        }
        countrs.close();
        count_stmt.close();
        System.out.println(count);
        String sql = "SELECT p.PRACOWNIK_ID, p.IMIE, p.NAZWISKO, p.PESEL, p.ZATRUDNIONY_OD, p.ZATRUDNIONY_DO, f.NAZWA_OFICJALNA from PRACOWNICY p INNER JOIN FILHARMONIE f on f.FILHARMONIA_ID = p.FILHARMONIA_ID"
                + " WHERE p.IMIE LIKE ? AND p.NAZWISKO LIKE ? AND p.PESEL LIKE ? AND f.NAZWA_OFICJALNA LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, imie+"%");
        stmt.setString(2, nazwisko+"%");
        stmt.setString(3, pesel+"%");
        stmt.setString(4, filharmonia+"%");
        ResultSet rs = stmt.executeQuery();
        Object [][] data = new Object[count][7];
        int index = 0;
        while(rs.next()){
            data[index][6] = (Object) rs.getInt(1);
            data[index][0] = (Object) rs.getString(2);
            data[index][1] = (Object) rs.getString(3);
            data[index][2] = (Object) rs.getString(4);
            data[index][3] = (Object) rs.getDate(5);
            data[index][4] = (Object) rs.getDate(6);
            data[index][5] = (Object) rs.getString(7);
            index++;
        }
        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(data, columnNames);
        tabelaPracownikow = new JTable(dm);
        tabelaPracownikow.setVisible(true);
        tabelaPracownikow.getColumn("EDYCJA").setCellRenderer(new ButtonRenderer());
        scrollPane = new JScrollPane(tabelaPracownikow);
        tabelaPracownikow.setFillsViewportHeight(true);
        kontentPanel.removeAll();
        kontentPanel.add(scrollPane);
        panelGlownyAdmin.repaint();
        rs.close();
        stmt.close();
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        private boolean isClicked = false;
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected && !isClicked) {
                try {
                    edytujPracownika((value == null) ? "" : value.toString());
                    isClicked = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
                isClicked = false;
            }
            setText("Edytuj");
            return this;
        }
    }

    protected void edytujPracownika(String id) throws SQLException {
        int id_prac = Integer.parseInt(id);
        dbHandler = DBHandler.wezInstancje();
        Connection connection = dbHandler.getDeskryptorPolaczenia();
        Statement stmt = connection.createStatement();
        String getPracownikSQL = "SELECT PRACOWNIK_ID, IMIE, NAZWISKO, DATA_URODZENIA, PLEC, NR_KONTA, PESEL, ZATRUDNIONY_OD, ZATRUDNIONY_DO from PRACOWNICY WHERE PRACOWNIK_ID=" + id_prac;
        ResultSet rs = stmt.executeQuery(getPracownikSQL);
        String imie = "";
        String nazwisko = "";
        char plec =' ';
        String pesel ="";
        Date dataUr =null, zatrOd = null, zatrDo=null;
        String nrKonta="";
        while (rs.next()){
            imie = rs.getString("IMIE");
            nazwisko = rs.getString("NAZWISKO");
            pesel = rs.getString("PESEL");
            plec = rs.getString("PLEC").charAt(0);
            dataUr = rs.getDate("DATA_URODZENIA");
            zatrOd = rs.getDate("ZATRUDNIONY_OD");
            zatrDo = rs.getDate("ZATRUDNIONY_DO");
            nrKonta = rs.getString("NR_KONTA");
        }
        System.out.println(dataUr);
        rs.close();
        stmt.close();
        frame.getContentPane().remove(kontentPanel);
        PracownikWidok widok = new PracownikWidok(this, id_prac, imie,nazwisko, pesel, plec, dataUr, zatrOd,zatrDo, nrKonta);
        kontentPanel = widok.getPanel1();
        kontentPanel.setVisible(true);
        panelGlownyAdmin.repaint();
        frame.getContentPane().add(kontentPanel);
        frame.revalidate();
    }


    private void dodajPracownika() {
    }

    private void znajdzPracownika() {
        frame.getContentPane().remove(kontentPanel);
        SzukajWidok widok = new SzukajWidok(this);
        kontentPanel = widok.getPanel1();
        kontentPanel.setVisible(true);
        panelGlownyAdmin.repaint();
        frame.getContentPane().add(kontentPanel);
        frame.revalidate();
    }

}
