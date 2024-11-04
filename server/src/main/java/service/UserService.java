package service;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import dataaccess.AuthDataAccess;
import model.AuthData;
import model.UserData;

import org.mindrot.jbcrypt.BCrypt;


public class UserService {
    private final UserDataAccess userdataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess){
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

        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        UserData user = new UserData(username, hashPassword, email);
        this.userdataAccess.addUser(user);
        AuthData auth = this.authDataAccess.createNewAuth(username);
        return auth.toString();

    }

    public UserData getUser(JsonObject body) throws DataAccessException{
//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        try {
            String username = body.get("username").toString().replaceAll("\"", "");
            return this.userdataAccess.getUserData(username);
        }
        catch (Exception e){
            throw new DataAccessException("cannot get user data", 401);
        }
    }

    public String checkAuth(JsonObject body) throws DataAccessException{
//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        UserData userData = this.getUser(body);
        if (userData == null){
            throw new DataAccessException("user does not exist", 401);
        }
        String password = body.get("password").toString().replaceAll("\"", "");

        if (BCrypt.checkpw(password, userData.password())){
            AuthData auth = this.authDataAccess.createNewAuth(userData.username());
            return auth.toString();
        }
        throw new DataAccessException("", 401);
    }

    public String logout(String authToken) throws DataAccessException{
//        String authToken = req.headers("Authorization");
        String user = this.authDataAccess.getUserByAuth(authToken);
        if (user == null){
            throw new DataAccessException("user unauthourized", 401);
        }
        this.authDataAccess.deleteAuth(authToken);
        return "{}";
    }

    public void clearDB() throws DataAccessException{
        try {
            userdataAccess.clear();
            authDataAccess.clear();
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

}