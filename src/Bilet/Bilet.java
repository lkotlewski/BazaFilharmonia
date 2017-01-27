package Bilet;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Łukasz on 2017-01-27.
 */
public class Bilet {
    private String tytul ;
    private Timestamp dataK ;
    private BigDecimal cena ;

    public Bilet() {
    }

    public Bilet(String tytul, Timestamp dataK, BigDecimal cena) {
        this.tytul = tytul;
        this.dataK = dataK;
        this.cena = cena;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public Timestamp getDataK() {
        return dataK;
    }

    public void setDataK(Timestamp dataK) {
        this.dataK = dataK;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }
}
