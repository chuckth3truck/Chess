package model;

import chess.ChessGame;

import java.util.Objects;

public record gameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public gameData rename(String color, String username){
        if (Objects.equals(color, "white")){
            return new gameData(gameID, username, blackUsername, gameName, game);
        }
        else{
            return new gameData(gameID, whiteUsername, username, gameName, game);
        }
    }
}
