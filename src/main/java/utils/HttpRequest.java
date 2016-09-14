package utils;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequest {
    private static final String[] POSSIBLE_METHODS = {"GET", "HEAD"};
    private boolean isValid;
    private String method;
    private String path;

    public HttpRequest(String request) {
        isValid = true;
        final String[] requestParts = request.split(" ", 3);
        if (requestParts.length == 3) {
            defineMethod(requestParts[0]);
            definePath(requestParts[1]);
        } else {
            isValid = false;
        }
    }

    private void defineMethod(String receivedMethod) {
        for (String possibleMethod : POSSIBLE_METHODS) {
            if (receivedMethod.equals(possibleMethod)) {
                method = receivedMethod;
            }
        }

        if (method == null) {
            isValid = false;
        }
    }

    private void definePath(String receivedUri) {
        try {
            final URI uri = new URI(receivedUri);
            if (receivedUri.endsWith(".") || receivedUri.contains("./") || receivedUri.contains("/.")) {
                throw new URISyntaxException("","");
            }
            path = uri.getPath();
        } catch (URISyntaxException e) {
            isValid = false;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
