package dataaccess;

import com.google.gson.JsonObject;
import model.authData;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class authDataDAOMemory  implements authDataAccess{
    private final HashMap<String, authData> authInfo = new HashMap<>();

    @Override
    public authData createNewAuth(String username) {
        String token = UUID.randomUUID().toString();
        authData auth = new authData(token, username);
        authInfo.put(username, auth);
        return auth;
    }

    @Override
    public String getUser(String authToken) throws DataAccessException {
        for (authData auth: authInfo.values()){
            if (Objects.equals(auth.authToken(), authToken)){
                return auth.username();
            }

        }
        throw new DataAccessException("This authToken does not exist", 401);
    }

    @Override
    public void clear() {
        authInfo.clear();
    }
}
