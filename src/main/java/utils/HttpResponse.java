package utils;

import server.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("LocalVariableHidesMemberVariable")
public class HttpResponse {
    private byte[] headers;
    private byte[] body;
    private String method = "";

    public HttpResponse(Server server, Status status, String method, Path path) throws IOException {
        this.method = method;
        this.body = Files.readAllBytes(path);

        final String extension = path.toString().substring(path.toString().lastIndexOf('.') + 1);
        final HttpHeaders headers = new HttpHeaders(
                status,
                server.getServerName(),
                body.length,
                new ContentType(extension)
        );
        this.headers = headers.toString().getBytes();
    }

    public HttpResponse(Server server, Status status) {
        this.body = buildErrorPage(status).getBytes();

        final HttpHeaders headers = new HttpHeaders(
                status,
                server.getServerName(),
                body.length,
                new ContentType("html")
        );
        this.headers = headers.toString().getBytes();
    }

    public byte[] toByteArray() {
        if (!method.equals("HEAD")) {
            final byte[] array = new byte[headers.length + body.length];
            System.arraycopy(headers, 0, array, 0, headers.length);
            System.arraycopy(body, 0, array, headers.length, body.length);

            return array;
        } else {
            return headers;
        }
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String buildErrorPage(Status status) {
        final StringBuilder errorPageBuilder = new StringBuilder();
        errorPageBuilder.append("<html><body><b>")
                .append(status.toString())
                .append("</b></body></html>");
        return errorPageBuilder.toString();
    }
}
