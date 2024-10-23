package dataaccess;

import model.userData;



public interface userDataAccess {
    userData getUserData(String username) throws DataAccessException;

    void addUser(userData userData);

    void clear();

    Boolean checkUserExists(String username);
}
