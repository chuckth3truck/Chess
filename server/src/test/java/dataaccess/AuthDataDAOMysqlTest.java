package dataaccess;

import model.authData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class AuthDataDAOMysqlTest {

    private authDataAccess authMemory;
    private authData auth;
    private String token;

    public AuthDataDAOMysqlTest() {

        try {
            authMemory = new AuthDataDAOMysql();
            System.out.println("using mysql auth");
        } catch (Exception e) {
            authMemory = new authDataDAOMemory();
        }
        this.auth = authMemory.createNewAuth("user");
        token = auth.authToken();
    }

    @Test
    @Order(1)
    void createNewAuth() {
        assertNotEquals(null, auth);
    }

    @Test
    @Order(2)
    void getUserByAuth() throws DataAccessException{
        assertDoesNotThrow(() -> authMemory.getUserByAuth(this.token));
        assertNotNull(authMemory.getUserByAuth(this.token));
        assertNull(authMemory.getUserByAuth("this is not an auth token"));

    }

    @Test
    @Order(3)
    void deleteAuth() throws DataAccessException{
        authMemory.deleteAuth(this.token);
        assertNull(authMemory.getUserByAuth(this.token));
    }

    @Test
    @Order(4)
    void clear() {
        assertDoesNotThrow(()->authMemory.clear());
    }
}