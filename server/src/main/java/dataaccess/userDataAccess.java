package dataaccess;

import com.google.gson.JsonObject;


public interface userDataAccess {
    String getUserData(String username) throws DataAccessException;

    String addUser(JsonObject userData) throws DataAccessException;

    String deleteUser(JsonObject userData) throws DataAccessException;

    String clear() throws DataAccessException;
}
