package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        try {
            serverFacade.clearData();
        }
        catch (ResponseException e){
            System.out.println(e.getMessage());
        }
        server.stop();
    }


    @Test
    public void loginTest() {
//        assertDoesNotThrow(() -> serverFacade.login("user1", "pass"));
        try {
            serverFacade = new ServerFacade("http://localhost:8080");

            serverFacade.login("user1", "pass");
        }
        catch (ResponseException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void registerTest() {
//        assertDoesNotThrow(() -> serverFacade.login("user1", "pass"));
        try {
            serverFacade = new ServerFacade("http://localhost:8080");

            serverFacade.registerUser("user1", "pass", "email");
        }
        catch (ResponseException e){
            System.out.println(e.getMessage());
        }
    }

}
