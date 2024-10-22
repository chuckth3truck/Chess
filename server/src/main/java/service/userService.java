package service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.userDataAccess;
import model.userData;
import spark.*;

public class userService {
    private final userDataAccess dataAccess;

    public userService(userDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public String createUser(Request req) throws DataAccessException{
        try{
            JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
            String username = body.get("username").toString();
            String password = body.get("password").toString();
            String email = body.get("email").toString();
            userData user = new userData(username, password, email);
            dataAccess.addUser(user);
            return user.toString();
        }
        catch (Exception e) {
            throw new DataAccessException("Bad Request", 400);
        }
    }

    public String getUser(String username){
        try{
            return dataAccess.getUserData(username).toString();
        }
        catch(Exception e){
            return null;
        }
    }

    public void clearDB(){
        dataAccess.clear();
    }

}
