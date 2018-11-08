package pack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DB {

    final String URL = "jdbc:derby:dzsuzDB;create=true";

    final String USERNAME = "";
    final String PASSWORD = "";

    private SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;

    public DB() {

        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("kapcsolat létrejött");
        } catch (SQLException ex) {
            System.out.println("Valami baj van a connection létrehozásakor.");
            System.out.println("" + ex);
        }


        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van van a createStatament létrehozásakor.");
                System.out.println("" + ex);
            }
        }


        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a DatabaseMetaData (adatbázis leírása) létrehozásakor..");
            System.out.println("" + ex);
        }

        try {
            ResultSet rs = dbmd.getTables(null, "APP", "TERMEKEK", null);
            if (!rs.next()) {
                createStatement.execute("create table termekek(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),termekNev varchar(50))");
            }

            rs = dbmd.getTables(null, "APP", "ELEMEK", null);
            if (!rs.next()) {
                createStatement.execute("create table elemek(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),elemNev varchar(50))");
            }

            rs = dbmd.getTables(null, "APP", "TERMEKELEM", null);
            if (!rs.next()) {
                createStatement.execute("create table termekelem(termekId int not null, elemId int not null, gr smallint)");
            }

            rs = dbmd.getTables(null, "APP", "NEVEK", null);
            if (!rs.next()) {
                createStatement.execute("create table nevek(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), nev varchar(50))");
            }

            rs = dbmd.getTables(null, "APP", "ELADAS", null);
            if (!rs.next()) {
                createStatement.execute("create table eladas(termekId int not null, nevekId int, datum varchar(25), kicsinagy smallint)"); //0= kicsi 1 = nagy
            }



        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattáblák létrehozásakor.");
            System.out.println("" + ex);
        }

        if(megnez()){
            elemekFeltolt();
        }



    }

    public boolean megnez(){
        String sql = "select * from elemek";
        try {
            ResultSet rs = createStatement.executeQuery(sql);

            while(rs.next()){
                return false;
            }
        } catch (SQLException e) {
            System.out.println("valami baj van a lekérdezéssel "+e);
        }
        return true;
    }

    public void elemekFeltolt(){
        try {
            String sql = "insert into elemek (elemNev) values "
                    + "('Alma'),('Ananász'),('Avokádó'),('Barack'),('Birsalma'),"
                    + "('Blue Majik'),('Brokkoli'),('Búzafű'),('Cayenne bors'),('Cékla'),"
                    + "('Citrom'),('Cukkini'),('Dinnye'),('Édeskömény'),('Eper'),"
                    + "('Fahéj'),('Grapefruit'),('Gyömbér'),('Káposzta'),('Kiwi'),"
                    + "('Körte'),('Lime'),('Mangó'),('Menta'),('Narancs'),"
                    + "('Petrezselyem'),('Répa'),('Rómaisaláta'),('Sárgadinnye'),('Spenót'),"
                    + "('Spirulina'),('Sütőtök'),('Uborka'),('Zeller')";
            createStatement.execute(sql);
        } catch (SQLException e) {
            System.out.println("valami baj van az elemek feltötésekor"+e);
        }
    }

    public ObservableList<osszetevo> getElemek(){
        ObservableList<osszetevo> eList = FXCollections.observableArrayList();

        String sql = "select * from elemek";
        try {
            ResultSet rs = createStatement.executeQuery(sql);

            while(rs.next()){
                eList.add(new osszetevo(rs.getInt("id"), rs.getString("elemNev"),0));
            }
        } catch (SQLException e) {
            System.out.println("valami baj van a lekérdezéssel "+e);
        }

        return eList;
    }

    public void ujTermek(String nev, ObservableList elemek){
        ObservableList<osszetevo> dbelemek = elemek;

        int id = 0;
        String sql = "insert into termekek (termekNev) values (?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, nev);
            preparedStatement.execute();

            sql = "select * from termekek where termekNev = '"+nev+"'";
            ResultSet rs = createStatement.executeQuery(sql);

            while(rs.next()){
                id = rs.getInt("id");
            }

            if(id != 0){
                sql = "insert into termekelem (termekId, elemId, gr) values (?,?,?)";
                preparedStatement = conn.prepareStatement(sql);
                for (int i = 0; i < dbelemek.size(); i++) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, Integer.parseInt(dbelemek.get(i).getId()));
                    preparedStatement.setInt(3, Integer.parseInt(dbelemek.get(i).getGr()));
                    preparedStatement.execute();
                }
            }
            System.out.println("termék hozzáadva");
        } catch (Exception e) {
            System.out.println("valami probléma a termek hozzáadásakor");
            System.out.println(e);
        }
    }
    public ObservableList<dzsuz> getTermekNevek(){
        ObservableList<dzsuz> nevek = FXCollections.observableArrayList();

        String sql = "select * from termekek";
        try {
            ResultSet rs = createStatement.executeQuery(sql);

            while(rs.next()){
                nevek.add(new dzsuz(rs.getInt("id"), rs.getString("termekNev")));
                System.out.println(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("valami baj van a lekérdezéssel "+e);
        }
        return nevek;
    }

    public ObservableList<osszetevo> getOsszetevok(int termekId){
        ObservableList<osszetevo> ossze = FXCollections.observableArrayList();
        String sql = "select elemek.elemNev as nev, termekelem.gr as suly, termekelem.elemId as elemid from termekelem inner join elemek on termekelem.elemId = elemek.id where termekId = "+termekId;
        try {
            ResultSet rs = createStatement.executeQuery(sql);

            while(rs.next()){
                ossze.add(new osszetevo(rs.getInt("elemid"),rs.getString("nev"),rs.getInt("suly")));
            }
        } catch (SQLException e) {
            System.out.println("valami baj van a lekérdezéssel "+e);
        }
        return ossze;
    }

    public void elemTorles(int id){
        try {
            String sql = "delete from termekek where id ="+id;
            createStatement.execute(sql);
            sql = "delete from termekelem where termekId ="+id;
            createStatement.execute(sql);
        } catch (SQLException e) {
            System.out.println("valami baj van a törlés során"+e);
        }
    }

    public void kosarHozzaad(dzsuz dzs, String nev){

        if(!nev.equals("")){
            nevControll(nev);
        }

        int nevId = 0;

        String asd = "select * from nevek where nev = '"+nev+"'";

        try {
            ResultSet rs = createStatement.executeQuery(asd);

            while(rs.next()){
                nevId = rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("valami baj van a nevek lekérdezésével!!  "+e);
        }



        String sql = "insert into eladas (termekId, nevekId, datum, kicsinagy) values (?,?,?,?)";
        PreparedStatement preparedStatement;
        int kn = 0;
        if(dzs.getKn().equals("nagy")){
            kn = 1;
        }
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(dzs.getId()));
            preparedStatement.setInt(2, nevId);
            preparedStatement.setString(3, f.format(new Date()));
            preparedStatement.setInt(4, kn);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("valami baj van a vásárlás feltöltésével" +ex);
        }
    }
}
