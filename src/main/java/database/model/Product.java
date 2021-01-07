package database.model;

public class Product {
    public int id;
    public String name;
    public double price;
    public int ownerId;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product(int id, String name, double price, int ownerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ownerId = ownerId;
    }

    public void copyFrom(Product product) {
        this.id = product.id;
        this.name = product.name;
        this.price = product.price;
        this.ownerId = product.ownerId;
    }
}
