package client;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:8080");

    }

    @AfterAll
    static void stopServer() {
        try {
            serverFacade.clearData();
            System.out.println("cleared");
        }
        catch (ResponseException e){
            System.out.println(e.getMessage());
        }
        server.stop();
    }

    @Test
    @Order(1)
    public void registerLoginTest() {
        AuthData auth;

        assertDoesNotThrow(() -> serverFacade.registerUser("user1", "pass", "email1"));
        assertDoesNotThrow(() -> serverFacade.login("user1", "pass"));

//        try {
//            serverFacade.registerUser("user1", "pass", "email");
//        }
//        catch (ResponseException e){
//            System.out.println(e.getMessage());
//        }
    }

}
