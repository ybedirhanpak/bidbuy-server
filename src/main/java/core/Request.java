package core;

public class Request {
    public String identifier;
    public Object body;
    public int subscriptionSubject;

    public Request(String identifier, Object body) {
        this.identifier = identifier;
        this.body = body;
        this.subscriptionSubject = -1;
    }

    public Request(String identifier, Object body, int subscriptionSubject) {
        this.identifier = identifier;
        this.body = body;
        this.subscriptionSubject = subscriptionSubject;
    }
}
