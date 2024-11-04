package dataaccess;

import model.GameData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class GameDAOMysqlTest {
    private GameDataAccess gameMemory;

    public GameDAOMysqlTest() {
        try{
            gameMemory = new GameDAOMysql();
            System.out.println("using mysql game");

        }
        catch (Exception e){
            gameMemory = new GameDAOMemory();
            System.out.println(e.getMessage());
        }

    }

    @Test
    @Order(1)
    void createGame() {
        assertEquals(101, gameMemory.createGame("game name"));
        assertEquals(102, gameMemory.createGame("game name 2"));
    }

    @Test
    @Order(2)
    void checkGameExists() {
        assertDoesNotThrow(()->gameMemory.checkGameExists(101));
        try{
            gameMemory.checkGameExists(103);
        }
        catch (DataAccessException e){
            assertEquals(400, e.getErrorCode());
        }
    }

    @Test
    @Order(3)
    void addPlayer() {
        assertDoesNotThrow(()->gameMemory.addPlayer("WHITE", 101, "user1"));
        assertDoesNotThrow(()->gameMemory.addPlayer("BLACK", 101, "user1"));

        try{
            gameMemory.addPlayer("WHITE", 101, "user3");
        }
        catch (DataAccessException e){
            assertEquals(403, e.getErrorCode());
        }
    }

    @Test
    @Order(4)
    void getGames() throws DataAccessException{
        assertDoesNotThrow(()->gameMemory.getGames());
        ArrayList<GameData> games = gameMemory.getGames().get("games");
        assertEquals(2, games.size());
    }

    @Test
    @Order(5)
    void clear() {
        assertDoesNotThrow(()->gameMemory.clear());
    }
}