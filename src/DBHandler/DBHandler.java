package DBHandler;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Łukasz on 2017-01-23.
 */
public class DBHandler {
    private final static DBHandler instancja = new DBHandler();
    private final String login = "lkotlews";
    private final  String haslo = "lkotlews";
    private final String url = "jdbc:oracle:thin:@ora3.elka.pw.edu.pl:1521:ora3inf";
    private Connection deskryptorPolaczenia;

    private DBHandler(){}

    public Connection getDeskryptorPolaczenia() {
        return deskryptorPolaczenia;
    }

    public static DBHandler wezInstancje(){
        return instancja;
    }

    public void otworzPolaczenie()
    {
        try
        {
            deskryptorPolaczenia = DriverManager.getConnection(url, login, haslo);
            deskryptorPolaczenia.setAutoCommit(false);
            JOptionPane.showMessageDialog(null, "Połączono z bazą danych");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void zamknijPolaczenie()
    {
        if(deskryptorPolaczenia != null)
        {
            try
            {
                deskryptorPolaczenia.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                deskryptorPolaczenia = null;
            }
        }
    }
}
