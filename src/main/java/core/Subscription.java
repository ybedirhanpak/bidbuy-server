package core;

import java.io.DataOutputStream;

public class Subscription {
    public Request request;
    public DataOutputStream outputStream;

    public Subscription(Request request, DataOutputStream dataOutputStream) {
        this.request = request;
        this.outputStream = dataOutputStream;
    }
}
