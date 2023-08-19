public enum MetodoHttp {

    GET(200, "OK"),
    POST(201, "Created"),
    PUT(204, "No Content"),
    DELETE(204, "No Content");

    private final int statusCode;
    private final String reasonText;

    MetodoHttp(int statusCode, String reasonText) {
        this.statusCode = statusCode;
        this.reasonText = reasonText;
    }

    MetodoHttp(int statusCode) {
        this(statusCode, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonText() {
        return reasonText;
    }
}