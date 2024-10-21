package dataaccess;


import com.google.gson.JsonObject;
import model.userData;

import java.util.HashMap;

public class userDAOMemory implements userDataAccess{
    private final HashMap<String, userData> userInfo = new HashMap<>();

    @Override
    public String addUser(userData userdata) {
        String username = userdata.username();
        userInfo.put(username, userdata);
        return userdata.username();
    }

    @Override
    public String deleteUser(String username) {
        userInfo.remove(username);
        return "success";
    }

    @Override
    public String clear(){
        userInfo.clear();
        return "success";
    }

    public userData getUserData(String username) throws DataAccessException{
        if (userInfo.containsKey(username)){
            return userInfo.get(username);
        }
        throw new DataAccessException("username not in database");
    }

}
