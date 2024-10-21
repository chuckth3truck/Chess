package dataaccess;

import com.google.gson.JsonObject;

public interface authDataAccess {
    JsonObject createNewAuth(String username) throws DataAccessException;

    JsonObject getUser(String authToken) throws DataAccessException;

    String clear() throws DataAccessException;

}
