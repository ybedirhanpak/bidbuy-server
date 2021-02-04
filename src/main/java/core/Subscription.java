package core;

import java.io.DataOutputStream;

public class Subscription {
    public Request request;
    public DataOutputStream outputStream;
    public int subjectId;

    public Subscription(Request request, DataOutputStream dataOutputStream, int subjectId) {
        this.request = request;
        this.outputStream = dataOutputStream;
        this.subjectId = subjectId;
    }
}
