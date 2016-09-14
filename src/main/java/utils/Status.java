package utils;

@SuppressWarnings("ConstantNamingConvention")
public class Status {
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;

    private final String description;

    public Status(int statusCode) {
        switch (statusCode) {
            case OK:
                description = "200 OK";
                break;
            case BAD_REQUEST:
                description = "400 Bad Request";
                break;
            case NOT_FOUND:
                description = "404 Not Found";
                break;
            default:
                description = "Error ocured";
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
