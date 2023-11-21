package activeRecord;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class TestDBConnection {

    @BeforeEach
    public void setUp(){

    }

    @Test
    public void test_getConnection() throws SQLException {
        Connection c1 = DBConnection.getConnection();
        Connection c2 = DBConnection.getConnection();
        assertEquals(c1, c2, "les 2 instances de Connections doivent être le même");
    }

    @Test
    public void test_setNomDB() throws SQLException {
        // verif si nom fournie == null
        try{
            DBConnection.setNomDB(null);
            fail("Une exeception aurait dû être levée");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }


        // verif si nom fournie pas database valide*
        DBConnection.setNomDB("aaaa");
        try{
            Connection c4 = DBConnection.getConnection();
            fail("Une exeception aurait dû être levée");
        } catch (SQLException e) {
            assertNotNull(e.getMessage());
        }


        // verif si nom fournie == nom db actuelle
        Connection c1 = DBConnection.getConnection();
        DBConnection.setNomDB("testpersonne");
        Connection c2 = DBConnection.getConnection();
        assertEquals(c1, c2, "les 2 instances de Connections doivent être égale");


        // verif si nom fournie != nom db actuelle
        DBConnection.setNomDB("testpersonne2");
        Connection c3 = DBConnection.getConnection();
        assertNotEquals(c1, c3, "les 2 instances de Connections doivent être différente");
    }
}
