package utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Sasha on 13.09.16.
 */
public class HttpRequest {
    private static final String[] POSSIBLE_METHODS = {"GET", "HEAD"};
    private boolean isValid;
    private String method;
    private String path;

    public HttpRequest(String request) {
        isValid = true;
        final String[] requestParts = request.split(" ", 3);
        defineMethod(requestParts[0]);
        definePath(requestParts[1]);
    }

    private void defineMethod(String method) {
        for (String possibleMethod : POSSIBLE_METHODS) {
            if (method.equals(possibleMethod)) {
                this.method = method;
            }
        }

        if (this.method == null) {
            isValid = false;
        }
    }

    private void definePath(String uri) {
        try {
            final URI parsedUri = new URI(uri);
            path = parsedUri.getPath();
        } catch (URISyntaxException e) {
            isValid = false;
            e.printStackTrace();
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
