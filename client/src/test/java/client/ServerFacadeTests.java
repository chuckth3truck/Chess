package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.glassfish.grizzly.utils.EchoFilter;
import org.junit.jupiter.api.*;

import server.Server;
import server.ServerFacade;

import java.sql.PreparedStatement;
import java.util.Random;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private  static int randNum;
    private static final String[] names = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + 0);
        serverFacade = new ServerFacade("http://localhost:"+port);
        Random random = new Random();
        randNum = abs(random.nextInt());



    }

    @AfterAll
    static void stopServer() {
        try {
            serverFacade.clearData();
        }
        catch (Exception e){
            System.out.println("could not clear data");
        }
        server.stop();

    }

    private static AuthData getAuth()throws Exception{
        return serverFacade.login(String.format("%d", randNum), "pass");
    }

    @Test
    @Order(1)
    public void registerTest() {

        assertDoesNotThrow(() -> serverFacade.registerUser(String.format("%d", randNum), "pass", "email1"));
        System.out.println("guy registered");

    }
    @Test
    @Order(2)
    public void badRegisterTest(){
        boolean except = false;
        try{
            serverFacade.registerUser(null, "pass", "email1");
        }
        catch (Exception e){
            except = true;
        }

        assert except;
    }

    @Test
    @Order(3)
    public void loginTest()throws Exception{
        serverFacade.registerUser("user1", "pass", "email1");
        assertDoesNotThrow(() -> serverFacade.login("user1", "pass"));
    }

    @Test
    @Order(4)
    public void badLoginTest(){
        boolean except = false;
        try{
            serverFacade.login("us1", "s");
        }
        catch (Exception e){
            except = true;
        }

        assert except;
    }

    @Test
    @Order(5)
    public void createGames() throws ResponseException, Exception{
        AuthData auth = getAuth();
        assertDoesNotThrow(() -> serverFacade.createGame(names[randNum%10], auth.authToken()));
    }

    @Test
    @Order(6)
    public void badCreateGames() {
        boolean except = false;
        try{
            serverFacade.createGame(names[randNum%10], null);
        }
        catch (Exception e){
            except = true;
        }

        assert except;

    }

    @Test
    @Order(7)
    public void listGames() throws Exception{
        AuthData auth = getAuth();
        assertDoesNotThrow(() -> serverFacade.listGames(auth.authToken()));
    }
    @Test
    @Order(8)
    public void badListGames() {
        boolean except = false;
        try{
            serverFacade.listGames(null);
        }
        catch (Exception e){
            except = true;
        }

        assert except;

    }


    @Test
    @Order(9)

    public void playGame() throws Exception{
        AuthData auth = getAuth();
        assertDoesNotThrow(() -> serverFacade.playGame(101, "white", auth.authToken()));
    }

    @Test
    @Order(10)

    public void badPlayGame() throws Exception{
        AuthData auth = getAuth();

        boolean except = false;
        try{
            serverFacade.playGame(101, null, auth.authToken());        }
        catch (Exception e){
            except = true;
        }

        assert except;
    }

    @Test
    @Order(11)
    public void logout()throws Exception{
        AuthData auth = getAuth();
        assertDoesNotThrow(() -> serverFacade.logout(auth.authToken()));
    }
    @Test
    @Order(12)
    public void badLogout(){
        boolean except = false;
        try{
            serverFacade.logout(null);        }
        catch (Exception e){
            except = true;
        }

        assert except;

    }

    @Test
    @Order(13)
    public void clearData()throws Exception{
        assert true;
    }
    @Test
    @Order(14)
    public void badClearData(){
        assert true;

    }







}
