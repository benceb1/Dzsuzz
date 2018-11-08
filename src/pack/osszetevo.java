package pack;

import javafx.beans.property.SimpleStringProperty;

public class osszetevo {
    private SimpleStringProperty id;
    private SimpleStringProperty nev;
    private SimpleStringProperty gr;
    private SimpleStringProperty grn;


    public osszetevo(int id, String nev, int gr) {
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.nev = new SimpleStringProperty(nev);
        this.gr = new SimpleStringProperty(String.valueOf(gr));
        this.grn = new SimpleStringProperty(String.valueOf(gr*2));
    }

    public String getNev(){
        return nev.get();
    }

    public void setNev(String nev){
        this.nev.set(nev);
    }

    public String getGr(){
        return gr.get();
    }

    public void setGr(String gr){
        this.gr.set(gr);
    }

    public String getGrn(){
        return grn.get();
    }

    public void setGrn(String grn){
        this.grn.set(grn);
    }

    public String getId(){
        return id.get();
    }

    public void setId(String id){
        this.id.set(id);
    }

    public int getGrszam(){
        return Integer.parseInt(gr.get());
    }

    public void grHozzaad(int i){
        int j = getGrszam();
        j+=i;
        setGr(String.valueOf(j));
    }
}
