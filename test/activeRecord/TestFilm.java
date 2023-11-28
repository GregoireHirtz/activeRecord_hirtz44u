package activeRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestFilm {


    @BeforeEach
    public void setUp() throws SQLException{
        String[] commandes = {
            "DROP TABLE IF EXISTS Film",

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

            "ALTER TABLE `Film`\n" +
                    "  ADD PRIMARY KEY (`id`),\n" +
                    "  ADD KEY `id_rea` (`id_rea`)",

            "ALTER TABLE `Film`\n" +
                    "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1",

            "ALTER TABLE `Film`\n" +
                    "  ADD CONSTRAINT `film_ibfk_1` FOREIGN KEY (`id_rea`) REFERENCES `Personne` (`id`)"
        };

        Connection connect = DBConnection.getConnection();
        for (int i=0; i<commandes.length; i++){
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(commandes[i]);
        }

    }

    @Test
    public void test_Film() throws SQLException {

    }
}
