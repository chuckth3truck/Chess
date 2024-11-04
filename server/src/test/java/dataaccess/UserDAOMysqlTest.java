package dataaccess;
import model.userData;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOMysqlTest {
    private userDataAccess userMemory;

    public UserDAOMysqlTest() {

        try {
            userMemory = new UserDAOMysql();
            System.out.println("using mysql user");
        } catch (Exception e) {
            userMemory = new userDAOMemory();
        }
    }



    @Test
    @Order(1)
    void addUser() {
        userData user = new userData("user","pass", "email@email");
        assertDoesNotThrow(() -> userMemory.addUser(user));
    }

    @Test
    @Order(2)
    void getUserData() {
        try {
            assertDoesNotThrow(() -> userMemory.getUserData("user"));
            assertNotEquals(null, userMemory.getUserData("user"));
            assertEquals(null, userMemory.getUserData("blue"));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    @Test
    @Order(3)
    void checkUserExists() {
        assertEquals(true, userMemory.checkUserExists("user"));
        assertEquals(false, userMemory.checkUserExists("blue"));
    }

    @Test
    @Order(4)
    void clear() {
        assertDoesNotThrow(() -> userMemory.clear());
    }
}