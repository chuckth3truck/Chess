package server;
import com.google.gson.*;
import model.userData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import service.*;
import dataaccess.*;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private final userService user;
    private final gameService game;


    public Server(){
        authDataAccess authMemory = new authDataDAOMemory();
        userDataAccess userMemory = new userDAOMemory();
        gameDataAccess gameMemory = new gameDAOMemory();

        this.user = new userService(userMemory, authMemory);
        this.game = new gameService(gameMemory, authMemory);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::addToGame);



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object createUser(Request req, Response res){

        try {
            return user.createUser(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object login(Request req, Response res){
        try {
            return user.checkAuth(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object logout(Request req, Response res){
        try {
            return user.logout(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object listGames(Request req, Response res){
        try {
            return game.listGames(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object createGame(Request req, Response res){
        try {
            return game.createGame(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object addToGame(Request req, Response res){
        try {
            return game.joinGame(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object clear(Request req, Response res){
        try {
            game.clearDB();
            user.clearDB();
        }

        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());
        }

        return "{}";
    }
}
