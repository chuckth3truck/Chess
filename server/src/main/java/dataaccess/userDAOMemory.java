package dataaccess;


import model.userData;

import java.util.HashMap;

public class userDAOMemory implements userDataAccess{
    private final HashMap<String, userData> userInfo = new HashMap<>();

    @Override
    public void addUser(userData userdata) {
        String username = userdata.username();
        userInfo.put(username, userdata);

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

    public Boolean checkUserExists(String username){
        return userInfo.containsKey(username);
    }

}
