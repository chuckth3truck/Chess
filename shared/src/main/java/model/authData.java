package model;

import com.google.gson.Gson;

public record authData(String authToken, String username) {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
