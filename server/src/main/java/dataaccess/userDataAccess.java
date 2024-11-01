package dataaccess;

import model.userData;



public interface userDataAccess {
    userData getUserData(String username) throws DataAccessException;

    void addUser(userData userData);

    void clear() throws DataAccessException;

    Boolean checkUserExists(String username);
}
