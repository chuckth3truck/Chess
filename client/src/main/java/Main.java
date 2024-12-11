import chess.*;
import client.ChessClient;
import server.Server;
import ui.EscapeSequences;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started HTTP server on " + port);
        System.out.println("♕ 240 Chess Client: " + EscapeSequences.WHITE_PAWN);

        ChessClient.main(args);

    }
}