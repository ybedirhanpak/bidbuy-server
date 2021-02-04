package core;

public class Request {
    public String identifier;
    public Object body;
    public boolean multipleResponse;

    public Request(String identifier, Object body) {
        this.identifier = identifier;
        this.body = body;
        this.multipleResponse = false;
    }

    public Request(String identifier, Object body, boolean multipleResponse) {
        this.identifier = identifier;
        this.body = body;
        this.multipleResponse = multipleResponse;
    }
}
