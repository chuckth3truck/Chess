package dataaccess;

import model.authData;

public interface authDataAccess {
    authData createNewAuth(String username);

    String getUserByAuth(String authToken) throws DataAccessException;

    void deleteAuth(String username);

    void clear();

}
