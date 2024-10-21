package dataaccess;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;


public interface userDataAccess {
    JsonObject getUserData(String username) throws DataAccessException;

    String addUser(JsonObject userData);

    String deleteUser(String username);

    String clear();
}
