package server;
import com.google.gson.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.delete("/Clear", this::clear);
        Spark.post("/user", this::createUser);



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
        JsonElement password = body.get("password");
        JsonElement email = body.get("email");
        return String.format("%s", user);
    }

}
