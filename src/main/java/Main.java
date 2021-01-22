import core.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(9000);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
