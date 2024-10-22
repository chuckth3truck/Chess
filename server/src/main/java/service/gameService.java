package service;
import dataaccess.DataAccessException;
import dataaccess.gameDataAccess;
import model.gameData;
import com.google.gson.*;

import java.util.HashMap;

public class gameService {
    private final gameDataAccess userdataAccess;

    public gameService(gameDataAccess userdataAccess){
        this.userdataAccess = userdataAccess;
    }

    public String createGame(String gameName){
        int game = userdataAccess.createGame(gameName);
        return String.format("{'gameID':%d}", game);
    }

    public String listGames(){
        return new Gson().toJson(userdataAccess.getGames());
    }

    public String joinGame(String playerColor, int gameID, String username){
        try{
            userdataAccess.addPlayer(playerColor, gameID, username);
        } catch (DataAccessException e) {
            return String.format("{'error':%s}", e);
        }
        return "{'success':{}}";
    }

    public void clearDB() throws DataAccessException {
        userdataAccess.clear();
    }




}
