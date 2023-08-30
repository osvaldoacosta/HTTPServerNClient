public enum HTTPStatus {

    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400,"Bad request"),
    NOT_FOUND(404,"Not Found"),
    METHOD_NOT_ALLOWED(405,"Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    NOT_IMPLEMENTED(501, "Not Implemented");


    private final int statusCode;
    private final String reasonText;

    HTTPStatus(int statusCode, String reasonText) {
        this.statusCode = statusCode;
        this.reasonText = reasonText;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public boolean isError() {
        return statusCode > 399 && statusCode < 600;
    }


}