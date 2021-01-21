package database.model;

public class Product {
    public int id;
    public String name;
    public double price;
    public int ownerId;
    public int lastBidId;
    public boolean isSold;
    public String imageURL;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product(int id, String name, double price, int ownerId, String imageURL) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ownerId = ownerId;
        this.imageURL = imageURL;
    }

    public void copyFrom(Product product) {
        this.id = product.id;
        this.name = product.name;
        this.price = product.price;
        this.ownerId = product.ownerId;
    }
}
