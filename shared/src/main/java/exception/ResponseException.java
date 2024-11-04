package exception;

public class ResponseException extends Exception {
    private final int errorCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.errorCode = statusCode;
    }

    public String getErrorMessage() {
        return String.format("{ \"message\": \"Error: %s\" }", getMessage());
    }

    public int getErrorCode() {
        return errorCode;
    }
}
