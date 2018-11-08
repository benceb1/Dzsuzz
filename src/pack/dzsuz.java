package pack;

import javafx.beans.property.SimpleStringProperty;

public class dzsuz {
    private String kn;
    private SimpleStringProperty id;
    private SimpleStringProperty nev;


    public  dzsuz(){

    }

    public dzsuz(int id, String nev){
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.nev = new SimpleStringProperty(nev);
        this.kn = "";
    }

    public dzsuz(int id, String nev, String kn){
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.nev = new SimpleStringProperty(nev);
        this.kn = new String(kn);
    }

    public String getKn(){
        return this.kn;
    }

    public void setKn(String kn){
        this.kn = kn;
    }

    public String getId(){
        return id.get();
    }

    public void setId(int id){
        this.id.set(String.valueOf(id));
    }

    public String getNev(){
        return nev.get();
    }

    public void setNev(String nev){
        this.nev.set(nev);
    }


    @Override
    public String toString(){
        if(this.kn != ""){
            return this.nev.get()+" ("+ (kn.equals("kicsi")? "k" : "n")+")";
        }
        return this.nev.get();
    }
}
