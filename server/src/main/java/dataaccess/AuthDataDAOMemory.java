package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDataDAOMemory implements AuthDataAccess {
    private final HashMap<String, AuthData> authInfo = new HashMap<>();
//    private final HashMap<String, String> authUserMap = new HashMap<>();

    @Override
    public AuthData createNewAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, username);
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
