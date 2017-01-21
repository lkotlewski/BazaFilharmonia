package WidokStart;

import Loger.Loger;
import Polaczenie.Polaczenie;
import WynikLaczenia.WynikLaczenia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;
import java.sql.*;

/**
 * Created by Łukasz on 2017-01-19.
 */
public class FormularzStartowy extends JFrame{

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



    public FormularzStartowy() {
        przyciskZaloguj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String haslo = hasloPole.getText();
                    String login = loginPole.getText();
                    int typ = listaTypowKont.getSelectedIndex() + 1;
                    Loger loger = new Loger(login, haslo, typ);
                    if (loger.zaloguj() == true)
                        JOptionPane.showMessageDialog(null, "Zalogowano pomyślnie");
                    else
                        JOptionPane.showMessageDialog(null, "Błędny login lub hasło, sprawdz czy dobrze wybrałeś typ konta");
                }
                catch(Exception wyj) {
                        JOptionPane.showMessageDialog(null, "Nie udało się połączyć z bazą danych");
                }
            }

        });
    }

    public static void main(String[] args) {

        try {

            //Polaczenie polaczenieStart = new Polaczenie();
            //wynik = polaczenieStart.polacz();
            //if (wynik.isCzyPolaczono()) {

                JFrame formularzStart = new JFrame("FormularzStartowy");
                formularzStart.setContentPane(new FormularzStartowy().panelGlowny);
                formularzStart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                formularzStart.pack();
                formularzStart.setVisible(true);
            //}
        }
        catch (Exception wyj) {
            wyj.printStackTrace();
        }


    }
}