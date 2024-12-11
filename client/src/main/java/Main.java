import chess.*;
import client.ChessClient;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started HTTP server on " + port);

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        ChessClient.main(args);

    }
}