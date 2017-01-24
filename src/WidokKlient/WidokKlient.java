package WidokKlient;

import DBHandler.DBHandler;
import RealizacjaKoncertu.RealizacjaKoncertu;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Created by Łukasz on 2017-01-22.
 */
public class WidokKlient  {
    private JPanel panel1;
    private JPanel panelGora;
    private JButton repertuarPrzycisk;
    private JButton wyswRezPrzycisk;
    private JPanel panelSrodek;
    private JTable tabelaWynikow;
    private JPanel panelKup;
    private JButton kupPrzycisk;
    private JComboBox comboBox1;
    private static Connection deskryptorPolaczenia;




    public WidokKlient() {
        repertuarPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabelaWynikow.setVisible(true);

                try {
                wyswietlRealizacje();
                }
                catch (Exception wyj) {
                    wyj.printStackTrace();

                }

            }
        });
    }

    public void wyswietlPanelKlienta() {
        JFrame frame = new JFrame("WidokKlient");
        frame.setContentPane(new WidokKlient().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public Vector<RealizacjaKoncertu> wezRealizacjeList()
    {
        Vector<RealizacjaKoncertu> realizacjeList = new Vector<RealizacjaKoncertu>();


        try {
            DBHandler mojhandler = DBHandler.wezInstancje();
            mojhandler.otworzPolaczenie();
            Statement zapytanie = mojhandler.getDeskryptorPolaczenia().createStatement();
            ResultSet spisRealizacji = zapytanie.executeQuery("SELECT * FROM Realizacje_Koncertu");
            RealizacjaKoncertu realizacja = new RealizacjaKoncertu();

            ResultSetMetaData metaData = spisRealizacji.getMetaData();
            Vector<String> nazwyKolumn = new Vector<>();
            int liczbaKolumn = metaData.getColumnCount();
            for (int nrKolumny = 1; nrKolumny <= liczbaKolumn; nrKolumny++) {
                nazwyKolumn.add(metaData.getColumnName(nrKolumny));
            }
            while(spisRealizacji.next())
            {
                realizacja = new RealizacjaKoncertu(spisRealizacji.getBigDecimal("realizacja_id"),spisRealizacji.getString("tytul"),
                        spisRealizacji.getTimestamp("data"),spisRealizacji.getBigDecimal("sala_id"),spisRealizacji.getBigDecimal("koncert_id"));
                realizacjeList.add(realizacja);
               // System.out.println(realizacja.getTytul());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return realizacjeList;
    }

    public void wyswietlRealizacje()
    {
        Vector<RealizacjaKoncertu> realizacjeList = wezRealizacjeList();
       // DefaultTableModel model = (DefaultTableModel)tabelaWynikow.getModel();
        Object[][] dane = new Object[realizacjeList.size()][2];
        while(tabelaWynikow.getRowCount() > 0)
        {
            ((DefaultTableModel) tabelaWynikow.getModel()).removeRow(0);
        }
        for(int i = 0; i < realizacjeList.size(); i++)
        {
           // dane[i][0] = realizacjeList.get(i).getRealizacjaID();
            dane[i][0] = realizacjeList.get(i).getTytul();
            dane[i][1] = realizacjeList.get(i).getData();
            //dane[i][3] = realizacjeList.get(i).getSalaID();
            //dane[i][4] = realizacjeList.get(i).getKoncertID();
        //    model.addRow(dane[i]);

        }
        for (int i = 0; i < realizacjeList.size(); i++
             ) {
            System.out.println(dane[i][0]);
        }
        Object[] nazwyKolumn = {"realizacja_id","tytul","data"};
        TableModel naszModel = new DefaultTableModel(dane,nazwyKolumn);
        tabelaWynikow.setModel(naszModel);
        tabelaWynikow.setOpaque(true);
       // tabelaWynikow.getColumn(2).getResizable();
       // tabelaWynikow.getColumn(2).setPreferredWidth(0);
       // tabelaWynikow.se
        /*tabelaWynikow.getColumn(0).setMaxWidth(0);
        tabelaWynikow.getColumn(1).setMinWidth(50);
        tabelaWynikow.getColumn(2).setMinWidth(50);
       */




    }

    public void FillTable(JTable table, String Query)
    {
        try
        {

            DBHandler mojhandler = DBHandler.wezInstancje();
            mojhandler.otworzPolaczenie();
            Statement zapytanie = mojhandler.getDeskryptorPolaczenia().createStatement();
            ResultSet spisRealizacji = zapytanie.executeQuery(Query);

            //To remove previously added rows
            while(table.getRowCount() > 0)
            {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = spisRealizacji.getMetaData().getColumnCount();
            while(spisRealizacji.next())
            {
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++)
                {
                    row[i - 1] = spisRealizacji.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(spisRealizacji.getRow()-1,row);
            }



        }
        catch(SQLException e)
        {
        }
    }

}
