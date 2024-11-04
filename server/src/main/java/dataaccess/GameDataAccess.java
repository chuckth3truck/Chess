package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDataAccess {
    HashMap<String, ArrayList<GameData>> getGames() throws DataAccessException;

    int createGame(String gameName);

    void checkGameExists(Integer gameID) throws DataAccessException;

    void addPlayer(String color, Integer gameID, String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
