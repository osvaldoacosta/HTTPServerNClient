public enum MetodosHTTP {

    GET(200),
    POST(201),
    PUT(204),
    DELETE(204);

    private final int code;

    MetodosHTTP(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}