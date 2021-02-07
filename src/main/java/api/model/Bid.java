package api.model;

import core.Util;
import database.DatabaseModel;

import java.util.Date;

public class Bid extends DatabaseModel {
    public int fromUserId;
    public int toProductId;
    public double price;
    public String createDate;

    public Bid(int fromUserId, int toProductId, double price) {
        this.fromUserId = fromUserId;
        this.toProductId = toProductId;
        this.price = price;
        this.createDate = Util.dateToString(new Date());
    }
}