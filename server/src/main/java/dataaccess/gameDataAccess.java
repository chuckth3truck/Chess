package dataaccess;

import com.google.gson.JsonObject;

import javax.xml.crypto.Data;

public interface gameDataAccess {
    JsonObject getGames() throws DataAccessException;

    JsonObject createGame(String gameName) throws DataAccessException;

    Boolean checkGameExists(String gameID) throws DataAccessException;

    String addPlayer(String color, String gameID) throws DataAccessException;

    String clear() throws DataAccessException;
}
