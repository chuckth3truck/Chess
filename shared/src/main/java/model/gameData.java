package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

public record gameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public gameData rename(String color, String username){
        String lColor = color.toLowerCase();
        if (Objects.equals(lColor, "white")){
            return new gameData(gameID, username, blackUsername, gameName, game);
        }
        else{
            return new gameData(gameID, whiteUsername, username, gameName, game);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
