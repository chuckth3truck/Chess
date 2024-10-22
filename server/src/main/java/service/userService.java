package service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.userDataAccess;
import dataaccess.authDataAccess;
import model.authData;
import model.userData;
import spark.*;

import java.util.Objects;
import java.util.Set;

public class userService {
    private final userDataAccess userdataAccess;
    private final authDataAccess authDataAccess;

    public userService(userDataAccess userDataAccess, authDataAccess authDataAccess){
        this.userdataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public String createUser(Request req) throws DataAccessException{
        try{
            JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
            String username = body.get("username").toString().replaceAll("\"", "");
            String password = body.get("password").toString().replaceAll("\"", "");
            String email = body.get("email").toString().replaceAll("\"", "");
            try{
                userData userData = this.getUser(req);
                authData auth = this.authDataAccess.createNewAuth(userData.username());
                return auth.toString();
            }
            catch (DataAccessException e) {
                userData user = new userData(username, password, email);
                this.userdataAccess.addUser(user);
                authData auth = this.authDataAccess.createNewAuth(username);
                return auth.toString();
            }
        }
        catch (Exception e) {
            throw new DataAccessException("did not get all required values", 400);
        }
    }

    public userData getUser(Request req) throws DataAccessException{
        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        String username = body.get("username").toString().replaceAll("\"", "");
        return this.userdataAccess.getUserData(username);
    }

    public String checkAuth(Request req) throws DataAccessException{
        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        userData userData = this.getUser(req);
        String password = body.get("password").toString().replaceAll("\"", "");
        if (Objects.equals(userData.password(), password)){
            if (this.authDataAccess.getUserByUsername(userData.username()) != null){
                return this.authDataAccess.getUserByUsername(userData.username()).toString();
            }
            authData auth = this.authDataAccess.createNewAuth(userData.username());
            return auth.toString();
        }
        throw new DataAccessException("", 401);
    }

    public String logout(Request req) throws DataAccessException{
        String authToken = req.headers("Authorization");
        String user = this.authDataAccess.getUserByAuth(authToken);
        this.authDataAccess.deleteAuth(user);
        return "{}";
    }

    public void clearDB(){
        userdataAccess.clear();
        authDataAccess.clear();
    }

}
