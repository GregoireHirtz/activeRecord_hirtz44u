package activeRecord;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestPersonne {

    @BeforeEach
    public void setUp() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String[] listeSql = {
                "DROP TABLE IF EXISTS Film",

                "DROP TABLE IF EXISTS Personne",

                "CREATE TABLE `Film` (\n" +
                        "  `id` int(11) NOT NULL,\n" +
                        "  `titre` varchar(40) NOT NULL,\n" +
                        "  `id_rea` int(11) DEFAULT NULL\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1",

                "INSERT INTO `Film` (`id`, `titre`, `id_rea`) VALUES\n" +
                        "(1, 'Arche perdue', 1),\n" +
                        "(2, 'Alien', 2),\n" +
                        "(3, 'Temple Maudit', 1),\n" +
                        "(4, 'Blade Runner', 2),\n" +
                        "(5, 'Alien3', 4),\n" +
                        "(6, 'Fight Club', 4),\n" +
                        "(7, 'Orange Mecanique', 3)",

                "CREATE TABLE `Personne` (\n" +
                        "  `id` int(11) NOT NULL,\n" +
                        "  `nom` varchar(40) NOT NULL,\n" +
                        "  `prenom` varchar(40) NOT NULL\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1",

                "INSERT INTO `Personne` (`id`, `nom`, `prenom`) VALUES\n" +
                        "(1, 'Spielberg', 'Steven'),\n" +
                        "(2, 'Scott', 'Ridley'),\n" +
                        "(3, 'Kubrick', 'Stanley'),\n" +
                        "(4, 'Fincher', 'David'),\n" +
                        "(5, 'Fincher', 'Tom')",

                "ALTER TABLE `Film`\n" +
                        "  ADD PRIMARY KEY (`id`),\n" +
                        "  ADD KEY `id_rea` (`id_rea`)",

                "ALTER TABLE `Personne`\n" +
                        "  ADD PRIMARY KEY (`id`)",

                "ALTER TABLE `Film`\n" +
                        "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8",

                "ALTER TABLE `Personne`\n" +
                        "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5",

                "ALTER TABLE `Film`\n" +
                        "  ADD CONSTRAINT `film_ibfk_1` FOREIGN KEY (`id_rea`) REFERENCES `Personne` (`id`)",

                "COMMIT"
        };

        for (int i=0; i<listeSql.length; i++){
            String sql = listeSql[i];
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(sql);
        }
    }


    @AfterEach
    public void unsetUp() throws SQLException{
        /**Connection connect = DBConnection.getConnection();

        String[] listeSql = {
                "DROP TABLE Film",
                "DROP TABLE Personne"
        };

        for (int i=0; i<listeSql.length; i++){
            String sql = listeSql[i];
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(sql);
        }*/
    }

    @Test
    public void test_Personne() throws SQLException {

        //test creation personne id -1
        Personne p1 = new Personne("nom1", "prenom1");
        assertEquals("nom1", p1.getNom(), "le nom devrait être 'nom1'");
        assertEquals("prenom1", p1.getPrenom(), "le prenom devrait être 'prenom1'");
        assertEquals(-1, p1.getId(), "l'id devrait être par défaut -1");

        // paramètre == null
        try{
            new Personne("nom1", null);
            fail("Une exeception aurait dû être levée");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
        try{
            new Personne(null, "prenom1");
            fail("Une exeception aurait dû être levée");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
        try{
            new Personne(null, null);
            fail("Une exeception aurait dû être levée");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void test_findAll() throws SQLException{
        ArrayList<Personne> liste = Personne.findAll();
        assertEquals(5, liste.size());

        Personne p1 = liste.get(0);
        Personne p2 = liste.get(2);

        assertEquals("Spielberg", p1.getNom());
        assertEquals("Steven", p1.getPrenom());
        assertEquals(1, p1.getId());

        assertEquals("Kubrick", p2.getNom());
        assertEquals("Stanley", p2.getPrenom());
        assertEquals(3, p2.getId());
    }

    @Test
    public void test_findById() throws SQLException{

        // test id fournie <= 0
        try{
            Personne.findById(0);
            fail("Une exeception aurait dû être levée");
        } catch (InvalidParameterException e) {
            assertNotNull(e.getMessage());
        }
        try{
            Personne.findById(-5);
            fail("Une exeception aurait dû être levée");
        } catch (InvalidParameterException e) {
            assertNotNull(e.getMessage());
        }

        Personne p2 = Personne.findById(1);
        assertEquals("Spielberg", p2.getNom());
        assertEquals("Steven", p2.getPrenom());
        assertEquals(1, p2.getId());

        Personne p3 = Personne.findById(100);
        assertNull(p3);
    }

    @Test
    public void test_findByName() throws SQLException{
        // test nom == null
        try{
            Personne.findByName(null);
            fail("Une exeception aurait dû être levée");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }


        ArrayList<Personne> liste1 = Personne.findByName("Spielberg");
        assertEquals(1, liste1.size());

        Personne p1 = liste1.get(0);
        assertEquals("Spielberg", p1.getNom());
        assertEquals("Steven", p1.getPrenom());
        assertEquals(1, p1.getId());


        ArrayList<Personne> liste2 = Personne.findByName("Fincher");
        assertEquals(2, liste2.size());

        Personne p2 = liste2.get(0);
        assertEquals("Fincher", p2.getNom());
        assertEquals("David", p2.getPrenom());
        assertEquals(4, p2.getId());

        Personne p3 = liste2.get(1);
        assertEquals("Fincher", p3.getNom());
        assertEquals("Tom", p3.getPrenom());
        assertEquals(5, p3.getId());


        ArrayList<Personne> liste3 = Personne.findByName("Test");
        assertEquals(0, liste3.size());
    }
}
