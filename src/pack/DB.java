package pack;

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



        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattáblák létrehozásakor.");
            System.out.println("" + ex);
        }


    }

}
