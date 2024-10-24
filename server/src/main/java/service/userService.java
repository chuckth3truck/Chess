package service;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.userDataAccess;
import dataaccess.authDataAccess;
import model.authData;
import model.userData;

import java.util.Objects;

public class userService {
    private final userDataAccess userdataAccess;
    private final authDataAccess authDataAccess;

    public userService(userDataAccess userDataAccess, authDataAccess authDataAccess){
        this.userdataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public String createUser(JsonObject body) throws DataAccessException{
        String username;
        String password;
        String email;
//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);

        try {
            username = body.get("username").toString().replaceAll("\"", "");
            password = body.get("password").toString().replaceAll("\"", "");
            email = body.get("email").toString().replaceAll("\"", "");
        }
        catch (Exception e) {
            throw new DataAccessException("bad request", 400);
        }

        if (this.userdataAccess.checkUserExists(username)){
            throw new DataAccessException("user already registered", 403);
        }

        userData user = new userData(username, password, email);
        this.userdataAccess.addUser(user);
        authData auth = this.authDataAccess.createNewAuth(username);
        return auth.toString();

    }

    public userData getUser(JsonObject body) throws DataAccessException{
//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        String username = body.get("username").toString().replaceAll("\"", "");
        return this.userdataAccess.getUserData(username);
    }

    public String checkAuth(JsonObject body) throws DataAccessException{
//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        userData userData = this.getUser(body);
        String password = body.get("password").toString().replaceAll("\"", "");
        if (Objects.equals(userData.password(), password)){
            authData auth = this.authDataAccess.createNewAuth(userData.username());
            return auth.toString();
        }
        throw new DataAccessException("", 401);
    }

    public String logout(String authToken) throws DataAccessException{
//        String authToken = req.headers("Authorization");
        this.authDataAccess.getUserByAuth(authToken);
        this.authDataAccess.deleteAuth(authToken);
        return "{}";
    }

    public void clearDB(){
        userdataAccess.clear();
        authDataAccess.clear();
    }

}
