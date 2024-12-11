package client;
import java.lang.module.ResolutionException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import exception.ResponseException;
import server.Server;

import model.AuthData;
import model.GameData;
import server.ServerFacade;
import model.UserData;
import ui.EscapeSequences;

public class ChessClient {

    private static boolean isLoggedIn = false;
    private static AuthData auth = null;
    private static final ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    private static HashMap<Integer,GameData> gamesList = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!isLoggedIn) {
                displayPreloginUI(scanner);
            } else {
                displayPostloginUI(scanner);
            }
        }
    }

    private static void displayPreloginUI(Scanner scanner) {
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

    private static void displayPostloginUI(Scanner scanner) {
        System.out.println("Available commands: Help, Logout, Create Game, List Games, Play Game, Observe Game, Clear");
        System.out.print("[LOGGED IN] >>> ");
        String command = scanner.nextLine().trim().toLowerCase();

        switch (command) {
            case "clear":
                handleClearData();
                break;
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

    private static String handleExceptions(ResponseException exception){
        if (exception.getErrorCode() == 401){
            return "unauthorized";
        }
        if (exception.getErrorCode() == 403){
            return "User already exists";
        }
        return "";
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
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
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

            var user = new UserData(username, password, email);

            auth = serverFacade.registerUser(username, password, email);
            isLoggedIn = true;
            System.out.println("Successfully registered and logged in.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void handleLogout() {
        try {
            serverFacade.logout(auth.authToken());
            isLoggedIn = false;
            auth = null;
            System.out.println("Successfully logged out.");
        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }

    private static void handleCreateGame(Scanner scanner) {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine().trim();

        try {
            serverFacade.createGame(gameName, auth.authToken());
            System.out.println("Game created successfully.");
        } catch (Exception e) {
            System.out.println("Failed to create game: " + e.getMessage());
        }
    }

    private static void updateGamesList(Integer gameId) throws ResponseException{
        GameData[] games = serverFacade.listGames(auth.authToken());

        if (gameId != null){
            gamesList.remove(gameId);
        }

        for (GameData game : games){
            if (!gamesList.containsKey(game.gameID())){
                gamesList.put(game.gameID(), game);
            }
        }

    }

    private static void handleListGames() {
        try {
            updateGamesList(null);
            System.out.println("Available games:\n" );
            for (Map.Entry<Integer, GameData> entry: gamesList.entrySet()){
                GameData game = entry.getValue();

                System.out.println("[" + game.gameID()%10 + "] " + game.gameName() + ", WHITE: " + game.whiteUsername()
                        + ", BLACK: " + game.blackUsername() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Failed to list games: " + e.getMessage());
        }
    }


    private static void handlePlayGame(Scanner scanner) {
        System.out.print("Enter game number: ");
        int gameNumber = Integer.parseInt(scanner.nextLine().trim()) + 100;
        System.out.print("Enter color (white/black): ");
        String color = scanner.nextLine().trim().toLowerCase();

        if (!gamesList.containsKey(gameNumber) && !gamesList.isEmpty()){
            System.out.println(gameNumber + " is not a valid game Number");
            return;
        }

        try {
            serverFacade.playGame(gameNumber, color, auth.authToken());
            updateGamesList(gameNumber);
            drawChessBoard(color.equals("white"));
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }

    private static void handleObserveGame(Scanner scanner) {
        System.out.print("Enter game number: ");
        int gameNumber = Integer.parseInt(scanner.nextLine().trim());

        try {
            serverFacade.observeGame(gameNumber, auth.authToken());
            drawChessBoard(true); // Observing as white perspective by default
        } catch (Exception e) {
            System.out.println("Failed to observe game: " + e.getMessage());
        }
    }

    private static void handleClearData(){
        try {
            serverFacade.clearData();
            System.out.println("DataBase Cleared");
        }
        catch (Exception e){
            System.out.println("Failed to Clear data: " + e.getMessage());
        }
    }

    private static void drawChessBoard(boolean isWhitePerspective) {
        String[] pieces = {"R", "N", "B", "Q", "K", "B", "N", "R"};
        String empty = " ";
        System.out.println("\nChess Board:");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean lightSquare = (i + j) % 2 == 0;
                System.out.print(lightSquare ? " " : "#");
            }
            System.out.println();
        }
    }


}
