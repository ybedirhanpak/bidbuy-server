package api.dto;

import api.model.Bid;
import core.Util;

import java.util.Date;

public class BidOut {
    public UserOut fromUser;
    public int toProductId;
    public double price;
    public String createDate;

    public BidOut(Bid bid, UserOut fromUser) {
        this.toProductId = bid.toProductId;
        this.price = bid.price;
        this.createDate = Util.dateToString(new Date());
        this.fromUser = fromUser;
    }
}
