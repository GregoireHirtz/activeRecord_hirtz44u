package activeRecord;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;

public class Personne {

    private int id;
    private String nom, prenom;

    public Personne(String n, String p){
        if (n==null || p==null) throw new NullPointerException("au 1 paramètre du constructeur vaut null");

        this.nom = n;
        this.prenom = p;
        this.id = -1;
    }

    private Personne(String n, String p, int id){
        if (n==null || p==null) throw new NullPointerException("au 1 paramètre du constructeur vaut null");
        if (id <= 0 ) throw new InvalidParameterException("la valeur de l'id ne peut être <= 0");

        this.nom = n;
        this.prenom = p;
        this.id = id;
    }

    public static ArrayList<Personne> findAll() throws SQLException {
        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT * FROM Personne";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        ArrayList<Personne> liste = new ArrayList<Personne>();
        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");
            Personne p = new Personne(nom, prenom, id);
            liste.add(p);
        }
        return liste;
    }


    public static Personne findById(int id) throws SQLException{
        if (id <= 0 ) throw new InvalidParameterException("id ne peut pas etre <= 0");

        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT * FROM Personne WHERE id LIKE ?";
        PreparedStatement prep = connect.prepareStatement(SQLPrep);
        prep.setInt(1, id);
        prep.execute();
        ResultSet rs = prep.getResultSet();
        Personne p = null;
        if (rs.next()){
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            p = new Personne(nom, prenom, id);
        }
        return p;
    }

    public static ArrayList<Personne> findByName(String nom) throws SQLException{
        if (nom==null) throw new NullPointerException("le nom fournie ne peut être égale à null");

        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT * FROM Personne WHERE nom LIKE ?";
        PreparedStatement prep = connect.prepareStatement(SQLPrep);
        prep.setString(1, nom);
        prep.execute();
        ResultSet rs = prep.getResultSet();
        ArrayList<Personne> liste = new ArrayList<Personne>();
        while (rs.next()) {
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");
            Personne p = new Personne(nom, prenom, id);
            liste.add(p);
        }
        return liste;
    }



    public String getNom(){return this.nom;}
    public String getPrenom(){return this.prenom;}
    public Integer getId(){return this.id;}
}
