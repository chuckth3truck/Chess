package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public AuthData registerUser(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws ResponseException{
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);


        return this.makeRequest("POST", "/session", body.toString(), AuthData.class, null);

    }

    public void logout(String auth) throws ResponseException {
        var path = "/Session";
        this.makeRequest("DELETE", path, null, null, auth);
    }

    public void clearData() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public GameData[] listGames() throws ResponseException {
        var path = "/pet";
        record listGameResponse(GameData[] games) {
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class, null);
        return response.games();
    }

    public Object createGame(String auth, String gameName){
        var path = "/game";
        JsonObject body = new JsonObject();
        body.addProperty("gameName", gameName);

        try {
            var response = this.makeRequest("POST", path, body, JsonObject.class, auth);
            return response;
        }
        catch (Exception e){
            return null;
        }

    }

    public void playGame(int gameNumber, String color, String auth) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("gameNumber", gameNumber);
        body.addProperty("color", color);

        makeRequest("PUT", "/game", body.toString(), JsonObject.class, auth);
    }

    public void observeGame(int gameNumber, String auth) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("gameNumber", gameNumber);

        makeRequest("PUT", "/game", body.toString(), null, auth);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            writeAuth(auth, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeAuth(String auth, HttpURLConnection http) throws IOException{
        if (auth != null){
            http.setRequestProperty("Authorization", auth);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
