package service;
import dataaccess.DataAccessException;
import dataaccess.gameDataAccess;
import model.gameData;
import com.google.gson.*;

import java.util.HashMap;

public class gameService {
    private final gameDataAccess dataAccess;

    public gameService(gameDataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public String createGame(String gameName){
        int game = dataAccess.createGame(gameName);
        return String.format("{'gameID':%d}", game);
    }

    public String listGames(){
        return new Gson().toJson(dataAccess.getGames());
    }

    public String joinGame(String playerColor, int gameID, String username){
        try{
            dataAccess.addPlayer(playerColor, gameID, username);
        } catch (DataAccessException e) {
            return String.format("{'error':%s}", e);
        }
        return "{'success':{}}";
    }

    public void clearDB() throws DataAccessException {
        dataAccess.clear();
    }




}
