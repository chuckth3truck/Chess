package dataaccess;

import chess.ChessGame;
import model.gameData;

import java.util.HashMap;


public class gameDAOMemory implements gameDataAccess{
    private final HashMap<Integer, gameData> gameInfo = new HashMap<>();


    @Override
    public HashMap<Integer, gameData> getGames(){
        return gameInfo;
    }

    @Override
    public int createGame(String gameName){
        int gameID = gameInfo.size() + 100;
        gameInfo.put(gameID, new gameData(gameID, "null", "null", gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public Boolean checkGameExists(Integer gameID) {
        return gameInfo.containsKey(gameID);
    }

    @Override
    public void addPlayer(String color, Integer gameID, String username) throws DataAccessException{
        if (!gameInfo.containsKey(gameID)){
            throw new DataAccessException("gameID does not exist", 401);
        }
        gameData game = gameInfo.get(gameID);
        gameInfo.remove(gameID);
        gameInfo.put(gameID, game.rename(color, username));
    }

    @Override
    public void clear(){
        gameInfo.clear();
    }
}
