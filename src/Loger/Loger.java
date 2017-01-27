package Loger;

import DBHandler.DBHandler;
import WidokKlient.WidokKlient;
import org.apache.commons.lang.mutable.MutableInt;

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


    public Loger(String login, String haslo, int typKonta) {
        this.login = login;
        this.haslo = haslo;
        this.typKonta = typKonta;
        //this.deskryptorPolaczenia = deskryptorPol;
    }

    public boolean zaloguj(MutableInt klientID){
        boolean czyZalogowano = false;
        boolean sprawdzajDalej = true;
        String url = "jdbc:oracle:thin:@ora3.elka.pw.edu.pl:1521:ora3inf";
        String user = "lkotlews";
        String password = "lkotlews";
        Integer klientNR = 0;

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
                    klientNR = spisKont.getInt("Konto_ID");
                    klientID.setValue(klientNR);

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
