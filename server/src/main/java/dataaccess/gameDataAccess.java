package dataaccess;

import com.google.gson.JsonObject;
import model.gameData;

import javax.xml.crypto.Data;
import java.util.HashMap;

public interface gameDataAccess {
    HashMap<Integer, gameData> getGames();

    int createGame(String gameName);

    Boolean checkGameExists(Integer gameID);

    void addPlayer(String color, Integer gameID, String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
