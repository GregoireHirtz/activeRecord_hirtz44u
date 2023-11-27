package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Connection instance;

    private String username="root";
    private String password="password";
    private String serverName="172.17.0.3";
    private String portNumber="3306";
    private static String dbName="testpersonne";



    private DBConnection() throws SQLException {
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);

        String url = "jdbc:mysql://" + serverName + ":"+portNumber + "/" + dbName;
        instance = DriverManager.getConnection(url, properties);
    }

    /**
     * méthode singleton pour connexion unique à la bd
     * @return instance Connection
     * @throws SQLException
     */
    public static synchronized Connection getConnection() throws SQLException{
        if (instance == null)
            new DBConnection();

        return instance;
    }

    /**
     * modifier le nom de la db cible
     * @param nomDB
     * @throws SQLException
     */
    public static synchronized void setNomDB(String nomDB) throws SQLException{
        if (nomDB == null)
            throw new NullPointerException("ERROR: nomDB = null");

        if ( nomDB != dbName){
            dbName = nomDB;
            instance = null;
        }
    }
}
