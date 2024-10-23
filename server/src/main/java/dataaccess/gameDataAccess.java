package dataaccess;

import com.google.gson.JsonObject;
import model.gameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;

public interface gameDataAccess {
    HashMap<String, ArrayList<gameData>> getGames();

    int createGame(String gameName);

    void checkGameExists(Integer gameID) throws DataAccessException;

    void addPlayer(String color, Integer gameID, String username) throws DataAccessException;

    void clear();
}
