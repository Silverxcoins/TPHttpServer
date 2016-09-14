package utils;

@SuppressWarnings("OverlyComplexMethod")
public class ContentType {
    final String contentType;

    public ContentType(String extension) {
        switch (extension) {
            case "html":
                contentType = "text/html";
                break;
            case "css":
                contentType = "text/css";
                break;
            case "js":
                contentType = "text/javascript";
                break;
            case "xml":
                contentType = "text/xml";
                break;
            case "txt":
                contentType = "text/txt";
                break;
            case "jpg":
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "swf":
                contentType = "application/x-shockwave-flash";
                break;
            default:
                contentType = "text/html";
        }
    }

    @Override
    public String toString() {
        return contentType;
    }
}
