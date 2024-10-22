package service;
import dataaccess.userDataAccess;
import model.userData;

public class userService {
    private final userDataAccess dataAccess;

    public userService(userDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public String createUser(String username, String password, String email){
        userData user = new userData(username, password, email);
        dataAccess.addUser(user);
        return user.toString();
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
