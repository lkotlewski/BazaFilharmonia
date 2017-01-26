package WidokKlient;

import DBHandler.DBHandler;
import RealizacjaKoncertu.RealizacjaKoncertu;
import org.apache.commons.lang.mutable.MutableInt;

import java.lang.Object;
import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.*;
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
    private JComboBox znizki;
    private JCheckBox czyZnizka;
    private static Connection deskryptorPolaczenia;
    private MutableInt klientID;

    public WidokKlient(MutableInt klientID) {
        this.klientID = klientID;
    }

    public MutableInt getKlientID() {
        return klientID;
    }

    public void setKlientID(MutableInt klientID) {
        this.klientID = klientID;
    }

    public WidokKlient() {
        repertuarPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabelaWynikow.setVisible(true);

                try {
                wyswietlRepertuar();
                }
                catch (Exception wyj) {
                    wyj.printStackTrace();

                }

            }
        });
        kupPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int znizkaID;
                BigDecimal realizacjaID;
                String polecenie;
                CallableStatement stmt = null;
                Vector<RealizacjaKoncertu> realizacjeList = wezRealizacjeList();
                int wybranaRealizcja = tabelaWynikow.getSelectedRow();
                if (wybranaRealizcja == -1)
                    JOptionPane.showMessageDialog(null,"Wybierz koncert na, który chcesz kupić bilet");
                else {
                    System.out.println(wybranaRealizcja);
                    if (czyZnizka.isSelected())
                        znizkaID = znizki.getSelectedIndex() + 1;
                    else
                        znizkaID = 0;
                    realizacjaID = realizacjeList.get(wybranaRealizcja).getRealizacjaID();
                    DBHandler mojhandler = DBHandler.wezInstancje();
                    mojhandler.otworzPolaczenie();

                    try {
                        String sql = "{call wystaw_bilet (?, ?, ?)}";
                        MutableInt mu = new MutableInt(getKlientID());
                        System.out.println(klientID.intValue());
                        int klientIDint = klientID.intValue();
                        BigDecimal klientID = new BigDecimal(klientIDint);
                        BigDecimal znizkaIDbigD = new BigDecimal(znizkaID);
                        stmt = mojhandler.getDeskryptorPolaczenia().prepareCall(sql);
                        stmt.setBigDecimal(1, realizacjaID);
                        stmt.setBigDecimal(2, klientID);
                        stmt.setBigDecimal(3,znizkaIDbigD);
                    //Use execute method to run stored procedure.
                       System.out.println("Executing stored procedure..." );
                       stmt.execute();
                   }
                   catch (Exception wyj){
                       wyj.printStackTrace();
                   }
                }

            }
        });
        czyZnizka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (czyZnizka.isSelected())
                    znizki.setVisible(true);
                else znizki.setVisible(false);
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
        Vector<RealizacjaKoncertu> realizacjeListFun= new Vector<RealizacjaKoncertu>();

        try {
            DBHandler mojhandler = DBHandler.wezInstancje();
            mojhandler.otworzPolaczenie();
            Statement zapytanie = mojhandler.getDeskryptorPolaczenia().createStatement();
            ResultSet spisRealizacji = zapytanie.executeQuery("SELECT * FROM Realizacje_Koncertu rea ORDER BY rea.data ASC");
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
                realizacjeListFun.add(realizacja);
               // System.out.println(realizacja.getTytul());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return realizacjeListFun;
    }

    public void wyswietlRepertuar()
    {
        Vector<RealizacjaKoncertu> realizacjeList = wezRealizacjeList();
        Object[][] dane = new Object[realizacjeList.size()][2];
        while(tabelaWynikow.getRowCount() > 0)
        {
            ((DefaultTableModel) tabelaWynikow.getModel()).removeRow(0);
        }
        for(int i = 0; i < realizacjeList.size(); i++)
        {
            dane[i][0] = realizacjeList.get(i).getTytul();
            dane[i][1] = realizacjeList.get(i).getData();
        }
        for (int i = 0; i < realizacjeList.size(); i++
             ) {
            System.out.println(dane[i][0]);
        }
        Object[] nazwyKolumn = {"tytul","data"};
        TableModel naszModel = new DefaultTableModel(dane,nazwyKolumn);
        tabelaWynikow.setModel(naszModel);
        tabelaWynikow.setOpaque(true);
        kupPrzycisk.setVisible(true);
        czyZnizka.setVisible(true);

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
