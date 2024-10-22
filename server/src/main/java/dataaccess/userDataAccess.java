package dataaccess;

import com.google.gson.JsonObject;
import model.userData;
import java.util.ArrayList;
import java.util.Collection;


public interface userDataAccess {
    userData getUserData(String username) throws DataAccessException;

    String addUser(userData userData);

    void deleteUser(String username);

    void clear();
}
