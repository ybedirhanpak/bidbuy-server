import core.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(9000);
        Thread serverThread = new Thread(server);
        serverThread.start();

        new Thread(() -> {
            Client c = new Client();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            c.sendRequest("getValue", "Hello world");
        }).start();
    }
}
