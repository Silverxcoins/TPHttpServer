package utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    public static final String HTTP_VERSION = "1.1";

    private final String statusDescription;
    private final Map<String, String> headers = new HashMap<>();

    public HttpHeaders(Status status, String server, int contentLength, ContentType contentType) {
        statusDescription = status.toString();
        headers.put("Date", (new Date().toString()));
        headers.put("Server", server);
        headers.put("Content-Length", String.valueOf(contentLength));
        headers.put("Content-Type", contentType.toString());
        headers.put("Connection", "close");
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/").append(HTTP_VERSION).append(' ').append(statusDescription).append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            stringBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }
}
