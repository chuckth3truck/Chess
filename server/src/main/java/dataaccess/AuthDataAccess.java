package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData createNewAuth(String username);

    String getUserByAuth(String authToken) throws DataAccessException;

    void deleteAuth(String username);

    void clear() throws DataAccessException;

}
