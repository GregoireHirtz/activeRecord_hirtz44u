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

    public void delete() throws SQLException{
        if (this.id != -1){
            Connection connect = DBConnection.getConnection();
            String commande = "DELETE FROM Personne WHERE id LIKE ?";
            PreparedStatement prep = connect.prepareStatement(commande);
            prep.setInt(1, this.id);
            prep.executeUpdate();
            this.id = -1;
        }
    }


    public void save() throws SQLException{
        if (this.id == -1){
            this.saveNew();
        }else{
            this.update();
        }
    }

    private void saveNew() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String commande = "INSERT INTO Personne (nom, prenom) VALUES (?, ?)";
        PreparedStatement prep = connect.prepareStatement(commande, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.executeUpdate();

        // recuperation de l'id autoincremente
        ResultSet rs = prep.getGeneratedKeys();
        if (rs.next()) {
            this.id = rs.getInt(1);
        }else{
            throw new SQLException("ERROR: id non renvoyé");
        }
    }

    private void update() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String commande = "UPDATE Personne SET nom=?, prenom=?";
        PreparedStatement prep = connect.prepareStatement(commande);
        prep.setString(1, this.nom);
        prep.setString(2, this.prenom);
        prep.executeUpdate();
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


    public static void createTable() throws SQLException{

        Connection connect = DBConnection.getConnection();

        String[] commandes = {
                "CREATE TABLE `Personne` (\n" +
                        "  `id` int(11) NOT NULL,\n" +
                        "  `nom` varchar(40) NOT NULL,\n" +
                        "  `prenom` varchar(40) NOT NULL\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1",

                "ALTER TABLE `Personne`\n" +
                        "  ADD PRIMARY KEY (`id`)",

                "ALTER TABLE `Personne`\n" +
                        "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5"
        };

        try{
            for (int i=0; i<commandes.length; i++){
                Statement stmt = connect.createStatement();
                stmt.executeUpdate(commandes[i]);
            }
        }catch (SQLException e){
            if (e.getErrorCode()==1050){
                throw new SQLException("Table déjà existante");
            }else{
                throw new SQLException();
            }
        }
    }


    public static void deleteTable() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String commande = "DROP TABLE IF EXISTS Personne";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(commande);
    }


    public String getNom(){return this.nom;}
    public String getPrenom(){return this.prenom;}
    public Integer getId(){return this.id;}

}
