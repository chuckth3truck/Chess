package server;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.*;
import service.*;
import dataaccess.*;

public class Server {
    private final UserService user;
    private final GameService game;


    public Server(){
        AuthDataAccess authMemory;
        try{
            authMemory = new AuthDataDAOMysql();
            System.out.println("using mysql auth");

        }
        catch (Exception e){
            authMemory = new AuthDataDAOMemory();
        }

        UserDataAccess userMemory;
        try{
            userMemory = new UserDAOMysql();
            System.out.println("using mysql user");
        }
        catch (Exception e){
            userMemory = new UserDAOMemory();
        }
        GameDataAccess gameMemory;
        try{
            gameMemory = new GameDAOMysql();
            System.out.println("using mysql game");

        }
        catch (Exception e){
            gameMemory = new GameDAOMemory();
            System.out.println(e.getMessage());
        }

        this.user = new UserService(userMemory, authMemory);
        this.game = new GameService(gameMemory, authMemory);
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
            JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
            return user.createUser(body);

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
            JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
            return user.checkAuth(body);
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
            String authToken = req.headers("Authorization");
            return user.logout(authToken);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage() + "logout");
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object listGames(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            return game.listGames(authToken);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage() + " list games");
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object createGame(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);

            return game.createGame(authToken, body);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage() + " create game");
            return e.getErrorMessage();
        }
        catch (Exception e){
            res.status(500);
            return String.format("{ \"message\": \"Error: %s\" }", e.getMessage());


        }
    }

    public Object addToGame(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
            return game.joinGame(authToken, body);
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
