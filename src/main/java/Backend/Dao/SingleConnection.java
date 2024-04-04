package Backend.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnection {
    //1 connexion à la BD
//    String db = "sql11670580";
    String db = "supermarche_db";
//    String user = "sql11670580";
    String user = "root";
//    String pwd = "LBqfAFAamU";
    String pwd = "";
    String url = "jdbc:mysql://localhost:3306/"+db;
//    String url = "jdbc:mysql://sql11.freesqldatabase.com:3306/"+db;
    private static Connection connection = null;
    private SingleConnection(){
        try {
            connection = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connexion réussie!");

        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        if(connection == null){
            new SingleConnection();
        }
        return connection;
    }

}