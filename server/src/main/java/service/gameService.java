package service;
import dataaccess.DataAccessException;
import dataaccess.authDataAccess;
import dataaccess.gameDataAccess;

import com.google.gson.*;



public class gameService {
    private final gameDataAccess gamedataAccess;
    private final dataaccess.authDataAccess authDataAccess;

    public gameService(gameDataAccess gamedataAccess, authDataAccess authDataAccess){
        this.gamedataAccess = gamedataAccess;
        this.authDataAccess = authDataAccess;
    }

    public String createGame(String authToken, JsonObject body) throws DataAccessException{
//        String authToken = req.headers("Authorization");
        String user = this.authDataAccess.getUserByAuth(authToken);
        if (user == null){
            throw new DataAccessException("unauthorized", 401);
        }

//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        String gameName = body.get("gameName").toString().replaceAll("\"", "");
        int game = this.gamedataAccess.createGame(gameName);
        return String.format("{ \"gameID\": %d }", game);
    }

    public String listGames(String authToken) throws DataAccessException{
//        String authToken = req.headers("Authorization");
        this.authDataAccess.getUserByAuth(authToken);
        return new Gson().toJson(this.gamedataAccess.getGames());
    }

    public String joinGame(String authToken, JsonObject body) throws DataAccessException{
//        String authToken = req.headers("Authorization");
        String username = this.authDataAccess.getUserByAuth(authToken);
        if (username == null){
            throw new DataAccessException("unauthorized", 401);
        }

//        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        if (body.get("playerColor") == null){
            throw new DataAccessException("bad player color", 400);
        }
        if(body.get("gameID") == null){
            throw new DataAccessException("bad player id", 400);
        }

        String playerColor = body.get("playerColor").toString().replaceAll("\"", "");
        int gameID = body.get("gameID").getAsInt();
        this.gamedataAccess.checkGameExists(gameID);

        this.gamedataAccess.addPlayer(playerColor, gameID, username);

        return "{}";
    }

    public void clearDB() throws DataAccessException{
        try {
            this.gamedataAccess.clear();
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage(), 500);
        }
    }




}
