package passoff.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dataaccess.*;
import model.userData;
import org.junit.jupiter.api.Test;
import service.*;
import spark.Request;
import com.google.gson.JsonObject;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTests {
    private final authDataAccess authMemory = new authDataDAOMemory();
    private final userDataAccess userMemory = new userDAOMemory();
    private final gameDataAccess gameMemory = new gameDAOMemory();

    private final userService user = new userService(userMemory, authMemory);
    private final gameService game = new gameService(gameMemory, authMemory);


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
        userData userdata = new userData("user1", "pass","test@blah.com");
        try {
            assert Objects.equals(this.user.getUser(userinfo), userdata);
        }
        catch (DataAccessException e){
            System.out.println("exception was thrown");
        }
    }

//    @Test
//    void checkAuth() {
//        JsonObject userinfo = new JsonObject();
//        userinfo.addProperty("password", "pass");
//        String res = "{"+
//                "\"authToken\": \"8f2fdf82-d4a5-4d97-88d1-4bf9eb49cbba\","+
//                "\"username\": \"user1\"}";
//
//        try {
//            assert Objects.equals(this.user.checkAuth(userinfo), res);
//        }
//        catch (DataAccessException e){
//            System.out.println("exception was thrown");
//        }
//    }

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