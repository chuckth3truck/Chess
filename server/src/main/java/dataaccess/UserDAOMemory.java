package dataaccess;


import model.UserData;

import java.util.HashMap;

public class UserDAOMemory implements UserDataAccess {
    private final HashMap<String, UserData> userInfo = new HashMap<>();

    @Override
    public void addUser(UserData userdata) {
        String username = userdata.username();
        userInfo.put(username, userdata);

    }


    @Override
    public void clear(){
        userInfo.clear();
    }

    public UserData getUserData(String username) throws DataAccessException{
        if (userInfo.containsKey(username)){
            return userInfo.get(username);
        }
        throw new DataAccessException("username not in database", 401);
    }

    public Boolean checkUserExists(String username){
        return userInfo.containsKey(username);
    }

}
