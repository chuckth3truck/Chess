package dataaccess;

import model.authData;

import java.util.HashMap;
import java.util.UUID;

public class authDataDAOMemory  implements authDataAccess{
    private final HashMap<String, authData> authInfo = new HashMap<>();
//    private final HashMap<String, String> authUserMap = new HashMap<>();

    @Override
    public authData createNewAuth(String username) {
        String token = UUID.randomUUID().toString();
        authData auth = new authData(token, username);
        authInfo.put(token, auth);
//        authUserMap.put(token, username);
        return auth;
    }

    @Override
    public String getUserByAuth(String authToken) throws DataAccessException {
        if (authInfo.containsKey(authToken)){
            return authInfo.get(authToken).username();
        }

        throw new DataAccessException("This authToken does not exist", 401);
    }

    @Override
    public void deleteAuth(String authToken) {
        authInfo.remove(authToken);
    }


    @Override
    public void clear() {
        authInfo.clear();
    }
}
