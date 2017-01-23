package WidokKlient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * Created by Łukasz on 2017-01-22.
 */
public class WidokKlient  {
    private JPanel panel1;
    private JPanel panelGora;
    private JButton repertuarPrzycisk;
    private JButton zarezerwujPrzycisk;
    private JButton wyswRezPrzycisk;
    private JPanel panelSrodek;
    private JList oknoWyswietl;
    private static Connection deskryptorPolaczenia;




    public WidokKlient() {
        repertuarPrzycisk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oknoWyswietl.setVisible(true);
                oknoWyswietl.setBackground(Color.black);
                //przewinPrzycisk.setVisible(true);
            }
        });
    }

    public void wyswietl() {
        JFrame frame = new JFrame("WidokKlient");
        frame.setContentPane(new WidokKlient().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
