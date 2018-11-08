package pack;

import javafx.beans.property.SimpleStringProperty;

public class Vevo {
    private int id;
    private String nev;

    public Vevo(int  id, String nev) {
        this.id = id;
        this.nev = nev;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nev;
    }
}
