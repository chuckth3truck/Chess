package client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;

import model.AuthData;
import model.GameData;
import server.ServerFacade;
import ui.EscapeSequences;

public class ChessClient {

    private static boolean isLoggedIn = false;
    private static AuthData auth = null;
    private static ServerFacade serverFacade;
    private static final HashMap<Integer,GameData> GAMES_LIST = new HashMap<>();


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        serverFacade = new ServerFacade(args[0]);

        while (true) {
            if (!isLoggedIn) {
                displayPreLoginUI(scanner);
            } else {
                displayPostLoginUI(scanner);
            }
        }
    }

    private static void displayPreLoginUI(Scanner scanner) {
        System.out.println("Available commands: Help, Login, Register, Quit");
        System.out.print("[LOGGED OUT] >>> ");
        String command = scanner.nextLine().trim().toLowerCase();

        switch (command) {
            case "help":
                displayHelp();
                break;
            case "login":
                handleLogin(scanner);
                break;
            case "register":
                handleRegister(scanner);
                break;
            case "quit":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Type 'Help' for available commands.");
        }
    }

    private static void displayPostLoginUI(Scanner scanner) {
        System.out.println("Available commands: Help, Logout, Create Game, List Games, Play Game, Observe Game");
        System.out.print("[LOGGED IN] >>> ");
        String command = scanner.nextLine().trim().toLowerCase();

        switch (command) {
            case "help":
                displayHelp();
                break;
            case "logout":
                handleLogout();
                break;
            case "create game":
                handleCreateGame(scanner);
                break;
            case "list games":
                handleListGames();
                break;
            case "play game":
                handlePlayGame(scanner);
                break;
            case "observe game":
                handleObserveGame(scanner);
                break;
            default:
                System.out.println("Invalid command. Type 'Help' for available commands.");
        }
    }

    private static void displayHelp() {
        System.out.println("\nHelp - Available Commands:");
        if (isLoggedIn){
            System.out.println("Commands: Help, Logout, Create Game, List Games, Play Game, Observe Game");
        }
        else {
            System.out.println("Commands: Help, Login, Register, Quit");
        }
    }

    private static void handleExceptions(ResponseException exception){
        if (exception.getErrorCode() == 401){
            System.out.println("unauthorized");
            return;
        }
        if (exception.getErrorCode() == 403){
            System.out.println("action failed");
            return;
        }
        System.out.println("action failed");
    }

    private static void handleLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            auth = serverFacade.login(username, password);
            isLoggedIn = true;
            System.out.println("Successfully logged in.");
            updateGamesList();
        } catch (Exception e) {
            if (e instanceof ResponseException){
                handleExceptions((ResponseException) e);
            }
            else{
                System.out.println("could not login");
            }
        }
    }
    private static void handleRegister(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        try {

            auth = serverFacade.registerUser(username, password, email);
            isLoggedIn = true;
            System.out.println("Successfully registered and logged in.");
        } catch (Exception e) {
            if (e instanceof ResponseException){
                handleExceptions((ResponseException) e);
            }
            else{
                System.out.println("could not register");
            }
        }
    }

    private static void handleLogout() {
        try {
            serverFacade.logout(auth.authToken());
            isLoggedIn = false;
            auth = null;
            System.out.println("Successfully logged out.");
        } catch (Exception e) {
            if (e instanceof ResponseException){
                handleExceptions((ResponseException) e);
            }
            else{
                System.out.println("could not logout");
            }
        }
    }

    private static void handleCreateGame(Scanner scanner) {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine().trim();

        try {
            serverFacade.createGame(gameName, auth.authToken());
            System.out.println("Game created successfully.");
        } catch (Exception e) {
            if (e instanceof ResponseException){
                handleExceptions((ResponseException) e);
            }
            else{
                System.out.println("could not create game");
            }
        }
    }

    private static void updateGamesList() throws ResponseException{
        GameData[] games = serverFacade.listGames(auth.authToken());

       GAMES_LIST.clear();

        for (GameData game : games){
            GAMES_LIST.put(game.gameID(), game);

        }
    }

    private static void handleListGames() {
        try {
            updateGamesList();
            System.out.println("Available games:\n" );
            for (Map.Entry<Integer, GameData> entry: GAMES_LIST.entrySet()){
                GameData game = entry.getValue();

                System.out.println("[" + game.gameID()%10 + "] " + game.gameName() + ", WHITE: " + game.whiteUsername()
                        + ", BLACK: " + game.blackUsername() + "\n");
            }
        } catch (Exception e) {
            if (e instanceof ResponseException){
                handleExceptions((ResponseException) e);
            }
            else{
                System.out.println("could not list games");
            }
        }
    }


    private static void handlePlayGame(Scanner scanner) {
        System.out.print("Enter game number: ");
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(scanner.nextLine().trim()) + 100;
        }
        catch (Exception e){
            System.out.println("invalid input");
            return;
        }
        System.out.print("Enter color (white/black): ");
        String color = scanner.nextLine().trim().toLowerCase();

        if (!GAMES_LIST.containsKey(gameNumber) && !GAMES_LIST.isEmpty()){
            System.out.println(gameNumber + " is not a valid game Number");
            return;
        }

        try {
            serverFacade.playGame(gameNumber, color, auth.authToken());
            updateGamesList();

            drawChessBoard(GAMES_LIST.get(gameNumber).game().getBoard(), color.equals("black"));

        } catch (Exception e) {
            if (e instanceof ResponseException){
                handleExceptions((ResponseException) e);
            }
            else{
                System.out.println("could not play game");
            }
        }
    }

    private static void handleObserveGame(Scanner scanner) {
        System.out.print("Enter game number: ");
        int gameNumber = Integer.parseInt(scanner.nextLine().trim()) + 100;

        try {
            drawChessBoard(GAMES_LIST.get(gameNumber).game().getBoard(), false); // Observing as white perspective by default
        } catch (Exception e) {
            System.out.println("Failed to observe game: " + e.getMessage());
        }
    }

    private static void drawChessBoard(ChessBoard board, boolean isWhite) {

//        mirror horizontally and verticaly for black perspecitive

        if (isWhite){
            board.resetBoard();
        }
        else{
            board.resetBlackBoard();
        }
        String columnLabel = getString(isWhite);

        System.out.println(columnLabel);
        for (int row = 0; row < 8; row++) {
            if (!isWhite) {
                System.out.print((8 - row) + " ");
            }
            else{
                System.out.print((row+1) + " ");
            }
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                }
                else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                }

                ChessPiece piece = board.getPiece(new ChessPosition(row+1, col+1));
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY); // Empty square
                }
                else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    System.out.print(getWhitePieces(piece));
                } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    System.out.print(getWBlackPieces(piece));
                }
            }
            if (!isWhite) {
                System.out.println("\u001B[0m" + " " + (8 - row));
            }
            else{
                System.out.println("\u001B[0m" + " " + (row+1));
            }
        }
        System.out.println(columnLabel);
    }

    private static String getString(boolean isWhite) {
        String columnLabel;

        if (isWhite){
            columnLabel = EscapeSequences.SET_BG_COLOR_LIGHT_GREY+ EscapeSequences.EMPTY +
                    String.format("%-4s", "H") + String.format("%-3s", "G") + String.format("%-4s", "F")
                    + String.format("%-3s", "E") + String.format("%-4s", "D") + String.format("%-4s", "C")
                    + String.format("%-3s", "B") + String.format("%-4s", "A") + EscapeSequences.RESET_BG_COLOR;
        }
        else{
            columnLabel = EscapeSequences.SET_BG_COLOR_LIGHT_GREY+ EscapeSequences.EMPTY +
                    String.format("%-4s", "A") + String.format("%-3s", "B") + String.format("%-4s", "C")
                    + String.format("%-3s", "D") + String.format("%-4s", "E") + String.format("%-4s", "F")
                    + String.format("%-3s", "G") + String.format("%-4s", "H") + EscapeSequences.RESET_BG_COLOR;
        }
        return columnLabel;
    }

    private static String getWhitePieces(ChessPiece piece){
        String pieceSequence = " ";
        switch (piece.toString().toLowerCase()) {
            case "k" -> pieceSequence = EscapeSequences.WHITE_KING;
            case "r" -> pieceSequence = EscapeSequences.WHITE_ROOK;
            case "q" -> pieceSequence = EscapeSequences.WHITE_QUEEN;
            case "p" -> pieceSequence = EscapeSequences.WHITE_PAWN;
            case "b" -> pieceSequence = EscapeSequences.WHITE_BISHOP;
            case "n" -> pieceSequence = EscapeSequences.WHITE_KNIGHT;

        }

        return pieceSequence;
    }

    private static String getWBlackPieces(ChessPiece piece) {
        String pieceSequence = " ";
        switch (piece.toString().toLowerCase()){
            case "k" -> pieceSequence = EscapeSequences.BLACK_KING;
            case "r" -> pieceSequence = EscapeSequences.BLACK_ROOK;
            case "q" -> pieceSequence = EscapeSequences.BLACK_QUEEN;
            case "p" -> pieceSequence = EscapeSequences.BLACK_PAWN;
            case "b" -> pieceSequence = EscapeSequences.BLACK_BISHOP;
            case "n" -> pieceSequence = EscapeSequences.BLACK_KNIGHT;
        }

        return pieceSequence;
    }


}
