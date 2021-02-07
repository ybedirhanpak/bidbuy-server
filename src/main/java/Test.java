import api.dto.BidCreate;
import api.dto.IdHolder;
import api.dto.ProductCreate;
import api.dto.UserAuth;

public class Test {
    public static void main(String[] args) {
        createUsers(50);
        createProducts(50);
    }

    public static void get(String request, int id, boolean isContinuous) {
        new Thread(() -> {
            Client client = new Client();
            IdHolder idHolder = new IdHolder(id);
            if (isContinuous) {
                client.sendContinuousRequest(request, idHolder, id);
            } else {
                client.sendRequest(request, idHolder);
            }
        }).start();
    }

    public static void delete(String request, int id) {
        new Thread(() -> {
            Client client = new Client();
            IdHolder idHolder = new IdHolder(id);
            client.sendRequest(request, idHolder);
        }).start();
    }


    // User
    public static void login(String username) {
        new Thread(() -> {
            Client client = new Client();
            UserAuth userAuth = new UserAuth(username, "password");
            client.sendRequest("login", userAuth);
        }).start();
    }

    public static void createUsers(int count) {
        for (int i = 0; i < count; i++) {
            String username = "testUser" + i;
            new Thread(() -> {
                Client client = new Client();
                UserAuth userAuth = new UserAuth(username, "password");
                client.sendRequest("register", userAuth);
            }).start();
        }
    }

    // Product
    public static void createProducts(int count) {
        String productName = "Test Product";
        int half = count / 2;
        int i = 0;
        for (; i < half; i++) {
            new Thread(() -> {
                Client client = new Client();
                ProductCreate createBody = new ProductCreate(productName, 10.99,
                        1, "https://productimages.hepsiburada.net/s/25/1100/10107307425842.jpg");
                client.sendRequest("createProduct", createBody);
            }).start();

            new Thread(() -> {
                Client client = new Client();
                ProductCreate createBody = new ProductCreate(productName, 10.99,
                        1, "https://productimages.hepsiburada.net/s/49/1100/10986386784306.jpg");
                client.sendRequest("createProduct", createBody);
            }).start();
        }

        if (count % 2 == 1) {
            new Thread(() -> {
                Client client = new Client();
                ProductCreate createBody = new ProductCreate(productName, 10.99,
                        1, "https://productimages.hepsiburada.net/s/49/1100/10986386784306.jpg");
                client.sendRequest("createProduct", createBody);
            }).start();
        }
    }

    // Bid
    public static void createBid(int userId, int productId, double price) {
        new Thread(() -> {
            Client client = new Client();
            BidCreate bidCreate = new BidCreate(userId, productId, price);
            client.sendRequest("createBid", bidCreate);
        }).start();
    }
}
