package api.model;

import database.DatabaseModel;

public class Product extends DatabaseModel {
    public String name;
    public double price;
    public int ownerId = -1;
    public int lastBidId = -1;
    public boolean isSold = false;
    public String imageURL = "";

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, double price, int ownerId, String imageURL) {
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
