package dataaccess;

import model.UserData;



public interface UserDataAccess {
    UserData getUserData(String username) throws DataAccessException;

    void addUser(UserData userData);

    void clear() throws DataAccessException;

    Boolean checkUserExists(String username);
}
