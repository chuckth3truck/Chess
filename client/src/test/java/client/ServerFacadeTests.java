package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

import server.Server;
import server.ServerFacade;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() throws Exception{
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + 0);
        serverFacade = new ServerFacade("http://localhost:"+port);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    public void registerLoginTest() {
        Random random = new Random();


        assertDoesNotThrow(() -> serverFacade.registerUser(String.format("%d", random.nextInt()), "pass", "email1"));
        assertDoesNotThrow(() -> serverFacade.login("user1", "pass"));


    }

    @Test
    @Order(2)
    public void createListGames() throws ResponseException{

        AuthData auth = serverFacade.login("user1", "pass");
        assertDoesNotThrow(() -> serverFacade.createGame("a", auth.authToken()));
        assertDoesNotThrow(() -> serverFacade.listGames(auth.authToken()));
        try {
            GameData[] games = serverFacade.listGames(auth.authToken());
            assert games.length > 0;
        }
        catch (Exception e){
            assert false;
        }
    }

    @Test
    @Order(4)
    public void playGame() throws ResponseException{
        AuthData auth = serverFacade.login("user1", "pass");
        assertDoesNotThrow(() -> serverFacade.playGame(101, "white", auth.authToken()));
    }







}
