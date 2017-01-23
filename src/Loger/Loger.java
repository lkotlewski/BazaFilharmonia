package Loger;

import DBHandler.DBHandler;

import javax.swing.*;
import java.io.Console;
import java.sql.*;

/**
 * Created by Łukasz on 2017-01-21.
 */
public class Loger {
    private String login;
    private String haslo;
    private int typKonta;
   // private static Connection deskryptorPolaczenia;

    public Loger(String login, String haslo, int typKonta) {
        this.login = login;
        this.haslo = haslo;
        this.typKonta = typKonta;
        //this.deskryptorPolaczenia = deskryptorPol;
    }

    public boolean zaloguj(){
        boolean czyZalogowano = false;
        boolean sprawdzajDalej = true;
        String url = "jdbc:oracle:thin:@ora3.elka.pw.edu.pl:1521:ora3inf";
        String user = "lkotlews";
        String password = "lkotlews";

        try {
            DBHandler mojhandler = DBHandler.wezInstancje();
            mojhandler.otworzPolaczenie();
            Statement zapytanie = mojhandler.getDeskryptorPolaczenia().createStatement();
            ResultSet spisKont = zapytanie.executeQuery("SELECT * FROM Konta");

            while (spisKont.next() && sprawdzajDalej){
                String log = spisKont.getString("Login");
                if (log.equals(this.login)) {
                    sprawdzajDalej = false;
                    String hasl = spisKont.getString("Haslo");
                    int typ = spisKont.getInt("Typ_ID");
                    if (hasl.equals(this.haslo)  && spisKont.getInt("Typ_ID") ==typKonta)
                        czyZalogowano = true;

                }
            }


        }
        catch (Exception wyjatek){
            wyjatek.printStackTrace();
        }
        finally {
            return czyZalogowano ;
        }
    }
}
