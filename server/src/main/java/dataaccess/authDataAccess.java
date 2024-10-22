package dataaccess;

import com.google.gson.JsonObject;
import model.authData;

public interface authDataAccess {
    authData createNewAuth(String username);

    String getUserByAuth(String authToken) throws DataAccessException;

    authData getUserByUsername(String Username);

    void deleteAuth(String username);

    void clear();

}
