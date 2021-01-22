package core;

import api.dto.Message;

public class Response {
    public int statusCode;
    public Object body;

    public Response() {
        this.statusCode = 500;
        this.body = new Message("Bad identifier");
    }

    public Response(Object body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }
}
