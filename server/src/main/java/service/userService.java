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
            String username = body.get("username").toString();
            String password = body.get("password").toString();
            String email = body.get("email").toString();
            userData user = new userData(username, password, email);
            userdataAccess.addUser(user);
            authData auth = authDataAccess.createNewAuth(username);
            return auth.toString();
        }
        catch (Exception e) {
            throw new DataAccessException("did not get all required values", 400);
        }
    }

    public userData getUser(Request req) throws DataAccessException{
        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        String username = body.get("username").toString();
        return userdataAccess.getUserData(username);
    }

    public String checkAuth(Request req) throws DataAccessException{
        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        userData userData = this.getUser(req);
        String password = body.get("password").toString();
        if (Objects.equals(userData.password(), password)){
            if (authDataAccess.getUserByUsername(userData.username()) != null){
                return authDataAccess.getUserByUsername(userData.username()).toString();
            }
            authData auth = authDataAccess.createNewAuth(userData.username());
            return auth.toString();
        }
        throw new DataAccessException("", 401);
    }

    public void clearDB(){
        userdataAccess.clear();
    }

}
