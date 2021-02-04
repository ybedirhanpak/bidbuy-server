package api.dto;

import api.model.Bid;
import api.model.Product;
import api.model.User;

public class ProductOut {
    public int id;
    public String name;
    public double price;
    public boolean isSold;
    public String imageURL;
    public BidOut lastBid;
    public UserOut owner;

    public ProductOut(Product product, UserOut owner, BidOut lastBid) {
        this.id = product.id;
        this.name = product.name;
        this.price = product.price;
        this.isSold = product.isSold;
        this.imageURL = product.imageURL;
        this.owner = owner;
        this.lastBid = lastBid;
    }
}
