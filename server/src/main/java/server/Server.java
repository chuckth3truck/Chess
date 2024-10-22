package server;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import service.*;
import dataaccess.*;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private final userService user;

    public Server(){
        this.user = new userService(new userDAOMemory());
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
        ;
        try {
            return user.createUser(req);
        }
        catch (DataAccessException e){
            res.status(e.getErrorCode());
            System.out.println(e.getErrorMessage());
            return e.getErrorMessage();
        }
    }

    public Object login(Request req, Response res){
        return null;
    }

    public Object logout(Request req, Response res){
        return null;
    }

    public Object listGames(Request req, Response res){
        return null;
    }

    public Object createGame(Request req, Response res){
        return null;
    }

    public Object addToGame(Request req, Response res){
        return null;
    }

    public Object clear(Request req, Response res){
        return null;
    }
}
