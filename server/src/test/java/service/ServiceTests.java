package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;


import java.util.Objects;

class ServiceTests {
    private final AuthDataAccess authMemory = new AuthDataDAOMemory();
    private final UserDataAccess userMemory = new UserDAOMemory();
    private final GameDataAccess gameMemory = new GameDAOMemory();

    private final UserService user = new UserService(userMemory, authMemory);
    private final GameService game = new GameService(gameMemory, authMemory);


    @Test
    void createUser() {
        JsonObject userinfo = new JsonObject();
        userinfo.addProperty("username", "user1");
        userinfo.addProperty("password", "pass");
        userinfo.addProperty("email", "test@blah.com");

        try{
            this.user.createUser(userinfo);
        }
        catch (DataAccessException e){
            System.out.println("exception was thrown");
        }
    }

    @Test
    void getUser() {
        JsonObject userinfo = new JsonObject();
        userinfo.addProperty("username", "user1");
        UserData userdata = new UserData("user1", "pass","test@blah.com");
        try {
            assert Objects.equals(this.user.getUser(userinfo), userdata);
        }
        catch (DataAccessException e){
            System.out.println("exception was thrown");
        }
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