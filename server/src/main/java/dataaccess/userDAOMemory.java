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
    public void deleteUser(String username) {
        userInfo.remove(username);
    }

    @Override
    public void clear(){
        userInfo.clear();
    }

    public userData getUserData(String username) throws DataAccessException{
        if (userInfo.containsKey(username)){
            return userInfo.get(username);
        }
        throw new DataAccessException("username not in database", 401);
    }

}
