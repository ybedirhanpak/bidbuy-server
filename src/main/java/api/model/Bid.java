package api.model;

import database.DatabaseModel;

public class Bid extends DatabaseModel {
    public int fromUserId;
    public int toProductId;
    public double price;

    public Bid(int fromUserId, int toProductId, double price) {
        this.fromUserId = fromUserId;
        this.toProductId = toProductId;
        this.price = price;
    }
}