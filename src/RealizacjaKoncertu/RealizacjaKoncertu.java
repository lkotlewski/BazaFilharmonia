package RealizacjaKoncertu;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Łukasz on 2017-01-24.
 */
public class RealizacjaKoncertu {
    BigDecimal RealizacjaID;
    String Tytul;
    Timestamp Data;
    BigDecimal SalaID;
    BigDecimal KoncertID;

    public RealizacjaKoncertu(BigDecimal realizacjaID, String tytul, Timestamp data, BigDecimal salaID, BigDecimal koncertID) {
        RealizacjaID = realizacjaID;
        Tytul = tytul;
        Data = data;
        SalaID = salaID;
        KoncertID = koncertID;
    }

    public RealizacjaKoncertu() {
    }

    public BigDecimal getRealizacjaID() {

        return RealizacjaID;
    }

    public void setRealizacjaID(BigDecimal realizacjaID) {
        RealizacjaID = realizacjaID;
    }

    public String getTytul() {
        return Tytul;
    }

    public void setTytul(String tytul) {
        Tytul = tytul;
    }

    public Timestamp getData() {
        return Data;
    }

    public void setData(Timestamp data) {
        Data = data;
    }

    public BigDecimal getSalaID() {
        return SalaID;
    }

    public void setSalaID(BigDecimal salaID) {
        SalaID = salaID;
    }

    public BigDecimal getKoncertID() {
        return KoncertID;
    }

    public void setKoncertID(BigDecimal koncertID) {
        KoncertID = koncertID;
    }
}
