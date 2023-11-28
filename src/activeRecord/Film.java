package activeRecord;

import java.security.InvalidParameterException;
import java.sql.*;

public class Film{

    private String titre;
    private int id, id_real;


    public Film(String t, Personne p){
        if (t==null) throw new NullPointerException("titre ne peut être = null");
        this.titre = t;
        this.id_real = p.getId();
        this.id = -1;
    }

    private Film(String t, int i, int i_r){
        if (t==null) throw new NullPointerException("titre ne peut être = null");
        this.titre = t;
        this.id_real = i_r;
        this.id = i;
    }

    public static Film findById(int id) throws SQLException {
        if (id <= 0 ) throw new InvalidParameterException("id ne peut pas etre <= 0");

        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT * FROM Film WHERE id LIKE ?";
        PreparedStatement prep = connect.prepareStatement(SQLPrep);
        prep.setInt(1, id);
        prep.execute();
        ResultSet rs = prep.getResultSet();
        Film f = null;
        if (rs.next()){
            String titre = rs.getString("titre");
            int id_rea = rs.getInt("id_rea");
            f = new Film(titre, id, id_rea);
        }
        return f;
    }

    public Personne getRealisateur() throws SQLException{
        return Personne.findById(this.id_real);
    }


    public static void createTable() throws SQLException{
        Connection connect = DBConnection.getConnection();

        String[] commandes = {
                "CREATE TABLE `Film` (\n" +
                        "  `id` int(11) NOT NULL,\n" +
                        "  `titre` varchar(40) NOT NULL,\n" +
                        "  `id_rea` int(11) DEFAULT NULL\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1",

                "ALTER TABLE `Film`\n" +
                        "  ADD PRIMARY KEY (`id`),\n" +
                        "  ADD KEY `id_rea` (`id_rea`)\n",

                "ALTER TABLE `Film`\n" +
                        "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0",

                "ALTER TABLE `Film`\n" +
                        "  ADD CONSTRAINT `film_ibfk_1` FOREIGN KEY (`id_rea`) REFERENCES `Personne` (`id`)"
        };

        try{
            for (int i=0; i<commandes.length; i++){
                Statement stmt = connect.createStatement();
                stmt.executeUpdate(commandes[i]);
            }
        }catch (SQLException e){

        }
    }


    public static void deleteTable() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String commande = "DROP TABLE IF EXISTS Film";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(commande);
    }


    public void save() throws SQLException, RealisateurAbsentException {
        if (this.id_real == -1) throw new RealisateurAbsentException();

        if (this.id != -1) {
            this.update();
        } else {
            this.saveNew();
        }
    }

    private void saveNew() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String commande = "INSERT INTO Film (titre, id_rea) VALUES (?, ?)";
        PreparedStatement prep = connect.prepareStatement(commande, Statement.RETURN_GENERATED_KEYS);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
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
        String commande = "UPDATE Film SET titre=?, id_rea=?";
        PreparedStatement prep = connect.prepareStatement(commande);
        prep.setString(1, this.titre);
        prep.setInt(2, this.id_real);
        prep.executeUpdate();
    }




    public void setTitre(String t){
        if (t==null) throw new NullPointerException("titre ne peut être = null");
        this.titre = t;
    }

    public String getTitre(){
        return this.titre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setId_real(int id_real){
        this.id_real = id_real;
    }

    public int getId_real(){
        return this.id_real;
    }
}
