package dataaccess;

import chess.ChessGame;
import model.gameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class gameDAOMemory implements gameDataAccess{
    private final HashMap<Integer, gameData> gameInfo = new HashMap<>();


    @Override
    public HashMap<String, ArrayList<gameData>> getGames(){
        var gameList = new ArrayList<gameData>(gameInfo.values());
        var games = new HashMap<String, ArrayList<gameData>>();
        games.put("games", gameList);
        return games;
    }

    @Override
    public int createGame(String gameName){
        int gameID = gameInfo.size() + 100;
        gameInfo.put(gameID, new gameData(gameID, "null", "null", gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public void checkGameExists(Integer gameID) throws DataAccessException {
        if (!gameInfo.containsKey(gameID)) {
            throw new DataAccessException("game does not exist", 400);
        }
        gameData game = gameInfo.get(gameID);
        if (!Objects.equals(game.blackUsername(), "null") && !Objects.equals(game.whiteUsername(), "null")){
            throw new DataAccessException("game taken", 403);
        }
    }

    @Override
    public void addPlayer(String color, Integer gameID, String username) throws DataAccessException{
        if (!gameInfo.containsKey(gameID)){
            throw new DataAccessException("gameID does not exist", 400);
        }
        gameData game = gameInfo.get(gameID);
        String lColor = color.toLowerCase();
        if (lColor.equals("white") && !Objects.equals(game.whiteUsername(), "null")){
            throw new DataAccessException("game already has white", 403);
        }
        if (lColor.equals("black") && !Objects.equals(game.blackUsername(), "null")){
            throw new DataAccessException("game already has black", 403);
        }


        gameInfo.remove(gameID);
        gameInfo.put(gameID, game.rename(color, username));
    }

    @Override
    public void clear(){
        gameInfo.clear();
    }
}
