package dataaccess;

import com.google.gson.JsonObject;
import model.authData;

public interface authDataAccess {
    authData createNewAuth(String username);

    String getUser(String authToken) throws DataAccessException;

    void clear();

}
