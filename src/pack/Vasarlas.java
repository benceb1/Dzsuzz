package pack;

import javafx.beans.property.SimpleStringProperty;

public class Vasarlas {

    private SimpleStringProperty id;
    private SimpleStringProperty nevId;
    private SimpleStringProperty ido;
    private SimpleStringProperty nev;
    private SimpleStringProperty kn;

    public Vasarlas(int id,int nevId, String ido, String nev, int kn) {
        this.ido = new SimpleStringProperty(ido);
        this.nev = new SimpleStringProperty(nev);
        this.kn = new SimpleStringProperty(kn == 0 ? "kicsi" : "nagy");
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.nevId = new SimpleStringProperty(String.valueOf(nevId));
    }

    public Vasarlas() {
        this.ido = new SimpleStringProperty("");
        this.nev = new SimpleStringProperty("");
        this.id = new SimpleStringProperty("");
        this.kn = new SimpleStringProperty("");
        this.nevId = new SimpleStringProperty("");
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getId() {
        return this.id.get();
    }

    public void setNevId(String id) {
        this.nevId.set(id);
    }

    public String getNevId() {
        return this.nevId.get();
    }

    public void setIdo(String ido) {
        this.ido.set(ido);
    }

    public String getIdo() {
        return this.ido.get();
    }

    public void setNev(String nev) {
        this.nev.set(nev);
    }

    public String getNev() {
        return this.nev.get();
    }

    public void setKn(String kn) {
        this.kn.set(kn);
    }

    public String getKn() {
        return this.kn.get();
    }
}
