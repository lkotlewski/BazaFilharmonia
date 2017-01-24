package WidokStart;

import Loger.Loger;
import WidokKlient.WidokKlient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;
import java.sql.*;

/**
 * Created by Łukasz on 2017-01-19.
 */
public class FormularzStartowy extends JFrame implements ActionListener{

    private JPanel panelGlowny;
    private JPanel panelDol;
    private JPanel panelLewo;
    private JPanel panelPrawo;
    private JLabel login;
    private JLabel haslo;
    private JLabel typKonta;
    private JTextField loginPole;
    private JPasswordField hasloPole;
    private JComboBox listaTypowKont;
    private JButton przyciskZaloguj;
    private JLabel napisPowitalny;
    private static Connection deskryptorPolaczenia;


    public FormularzStartowy(String title) {
        super(title);
        przyciskZaloguj.addActionListener(this);
    }
    public FormularzStartowy() {
        przyciskZaloguj.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            String haslo = hasloPole.getText();
            String login = loginPole.getText();
            int typ = listaTypowKont.getSelectedIndex() + 1;
            Loger loger = new Loger(login, haslo, typ);
            if (loger.zaloguj()){
                JOptionPane.showMessageDialog(null, "Zalogowano pomyślnie");
                if (typ == 3)
                {

                    WidokKlient widok = new WidokKlient();
                    new Thread(()->widok.wyswietlPanelKlienta()).start();
                    this.setVisible(false);
                    this.dispose();

                }
            }
            else
                JOptionPane.showMessageDialog(null, "Błędny login lub hasło, sprawdz czy dobrze wybrałeś typ konta");
        }
        catch(Exception wyj) {
            JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych");
        }
    }
    public JPanel getMainPanel()
    {
        return panelGlowny;
    }

    public static void main(String[] args) {

        try {

            FormularzStartowy formularzStart = new FormularzStartowy("FormularzStartowy");
            formularzStart.setContentPane(formularzStart.getMainPanel());
            formularzStart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            formularzStart.pack();
            formularzStart.setVisible(true);
            formularzStart.setResizable(false);
            //}
        }
        catch (Exception wyj) {
            wyj.printStackTrace();
        }


    }


}