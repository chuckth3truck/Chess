package server;
import com.google.gson.*;
import spark.*;

public class Server {

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
        JsonObject body = new Gson().fromJson(String.format("%s", req.body()), JsonObject.class);
        JsonElement user = body.get("username");
        return String.format("%s", user);
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
