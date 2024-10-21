package dataaccess;


import com.google.gson.JsonObject;


import java.util.HashMap;

public class userDAOMemory implements userDataAccess{
    private final HashMap<String, JsonObject> userInfo = new HashMap<>();

    @Override
    public String addUser(JsonObject userData) {
        String username = userData.get("username").toString();
        userInfo.put(username, userData);
        return userData.get("username").toString();
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

    public JsonObject getUserData(String username) throws DataAccessException{
        if (userInfo.containsKey(username)){
            return userInfo.get(username);
        }
        throw new DataAccessException("username not in database");
    }

}
