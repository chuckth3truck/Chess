package service;
import dataaccess.DataAccessException;
import dataaccess.authDataAccess;
import dataaccess.gameDataAccess;

import com.google.gson.*;
import spark.Request;


public class gameService {
    private final gameDataAccess gamedataAccess;
    private final dataaccess.authDataAccess authDataAccess;

    public gameService(gameDataAccess gamedataAccess, authDataAccess authDataAccess){
        this.gamedataAccess = gamedataAccess;
        this.authDataAccess = authDataAccess;
    }

    public String createGame(Request req) throws DataAccessException{
        String authToken = req.headers("Authorization");
        this.authDataAccess.getUserByAuth(authToken);

        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        String gameName = body.get("gameName").toString().replaceAll("\"", "");
        int game = this.gamedataAccess.createGame(gameName);
        return String.format("{ \"gameID\": %d }", game);
    }

    public String listGames(Request req) throws DataAccessException{
        String authToken = req.headers("Authorization");
        this.authDataAccess.getUserByAuth(authToken);
        return new Gson().toJson(this.gamedataAccess.getGames());
    }

    public String joinGame(Request req) throws DataAccessException{
        String authToken = req.headers("Authorization");
        String username = this.authDataAccess.getUserByAuth(authToken);

        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        String playerColor = body.get("playerColor").toString().replaceAll("\"", "");
        int gameID = body.get("gameID").getAsInt();
        this.gamedataAccess.checkGameExists(gameID);

        this.gamedataAccess.addPlayer(playerColor, gameID, username);

        return "{}";
    }

    public void clearDB() throws DataAccessException {
        this.gamedataAccess.clear();
    }




}
