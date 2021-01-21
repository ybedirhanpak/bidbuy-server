import api.dto.ProductCreate;
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

            ProductCreate productCreate = new ProductCreate(
                    "Test product",
                    10.99,
                    1,
                    "https://productimages.hepsiburada.net/s/49/1100/10986386784306.jpg"
            );

            c.sendRequest("getProductList", productCreate);
            c.sendRequest("createProduct", productCreate);
        }).start();
    }
}
