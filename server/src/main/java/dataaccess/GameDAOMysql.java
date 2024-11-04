package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class GameDAOMysql implements GameDataAccess {

    final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    int numGames = 1;

    public GameDAOMysql() throws DataAccessException{
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public HashMap<String, ArrayList<GameData>> getGames() throws DataAccessException{
        var result = new ArrayList<GameData>();
        var games = new HashMap<String, ArrayList<GameData>>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {

                        String game= DatabaseManager.readData(rs);
                        GameData gameInfo = new Gson().fromJson(game, GameData.class);
                        result.add(gameInfo);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        games.put("games", result);
        return games;
    }

    @Override
    public int createGame(String gameName) {
        var statement = "INSERT INTO game (gameID, json) VALUES (?, ?)";
        int gameID = numGames + 100;
        numGames += 1;
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        try {
            DatabaseManager.executeUpdate(statement, gameID, new Gson().toJson(game));
        }
        catch (Exception e){
            System.out.println("could not add user");
        }
        return gameID;
    }

    @Override
    public void checkGameExists(Integer gameID) throws DataAccessException {
        String game;
        try {
            game = DatabaseManager.getGameData(null, gameID, "SELECT gameID, json FROM game WHERE gameID=?");
        }
        catch (Exception e){
            throw new DataAccessException("could not get game data: " + e.getMessage(), 500);
        }

        if (game == null){
            throw new DataAccessException("game does not exist", 400);
        }

        GameData gameInfo = new Gson().fromJson(game, GameData.class);
        if (!Objects.equals(gameInfo.blackUsername(), null) && !Objects.equals(gameInfo.whiteUsername(), null)){
            throw new DataAccessException("game taken", 403);
        }
    }


    @Override
    public void addPlayer(String color, Integer gameID, String username) throws DataAccessException {
        String game;
        try {
            game = DatabaseManager.getGameData(null, gameID, "SELECT gameID, json FROM game WHERE gameID=?");
        } catch (Exception e) {
            throw new DataAccessException("could not get game data: " + e.getMessage(), 500);

        }
        GameData gameInfo = new Gson().fromJson(game, GameData.class);

        String lColor = color.toLowerCase();
        if (lColor.equals("white") && !Objects.equals(gameInfo.whiteUsername(), null)){
            throw new DataAccessException("game already has white", 403);
        }
        if (lColor.equals("black") && !Objects.equals(gameInfo.blackUsername(), null)){
            throw new DataAccessException("game already has black", 403);
        }

        String delStatement = String.format("DELETE FROM game WHERE gameID=%d", gameID);

        DatabaseManager.executeUpdate(delStatement);


        GameData newGame =gameInfo.rename(color, username);
        var addStatement = "INSERT INTO game (gameID, json) VALUES (?, ?)";

        try {
            DatabaseManager.executeUpdate(addStatement, gameID, new Gson().toJson(newGame));
        }
        catch (Exception e){
            System.out.println("could not add user: " + e.getMessage());
        }

    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

}
