package core;

public class Request {
    public String identifier;
    public Object body;

    public Request(String identifier, Object body) {
        this.identifier = identifier;
        this.body = body;
    }
}
