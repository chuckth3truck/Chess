package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameData rename(String color, String username){
        String lColor = color.toLowerCase();
        if (Objects.equals(lColor, "white")){
            return new GameData(gameID, username, blackUsername, gameName, game);
        }
        else{
            return new GameData(gameID, whiteUsername, username, gameName, game);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
