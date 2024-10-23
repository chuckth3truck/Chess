package passoff.service;

import dataaccess.*;
import org.junit.jupiter.api.Test;
import service.*;
import spark.Request;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {
    private final authDataAccess authMemory = new authDataDAOMemory();
    private final userDataAccess userMemory = new userDAOMemory();
    private final gameDataAccess gameMemory = new gameDAOMemory();

    private final userService user = new userService(userMemory, authMemory);
    private final gameService game = new gameService(gameMemory, authMemory);


    @Test
    void createUser() {
        user.createUser();
    }

    @Test
    void getUser() {
    }

    @Test
    void checkAuth() {
    }

    @Test
    void logout() {
    }


    @Test
    void createGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void clearDB() {
    }
}