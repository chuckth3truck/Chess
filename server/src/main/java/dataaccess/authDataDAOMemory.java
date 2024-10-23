package dataaccess;

import com.google.gson.JsonObject;
import model.authData;

import java.util.HashMap;
import java.util.Objects;
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
//        for (authData auth: authInfo.values()){
//            if (Objects.equals(auth.authToken(), authToken)){
//                return auth.username();
//            }
//
//        }
//        if (authUserMap.containsKey(authToken)){
//            String username = authUserMap.get(authToken);
//            if (authInfo.containsKey(username)) {
//                return authInfo.get(username).username();
//            }
//        }
        throw new DataAccessException("This authToken does not exist", 401);
    }

    @Override
    public authData getUserByUsername(String Username) {
//        if (authInfo.containsKey(Username)){
//            return authInfo.get(Username);
//        }
        for (authData auth: authInfo.values()) {
            if (Objects.equals(auth.username(), Username)) {
                return auth;
            }
        }
        return null;
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
