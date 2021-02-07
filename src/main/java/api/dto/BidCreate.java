package api.dto;

public class BidCreate {
    public int fromUserId;
    public int toProductId;
    public double price;

    public BidCreate(int fromUserId, int toProductId, double price) {
        this.fromUserId = fromUserId;
        this.toProductId = toProductId;
        this.price = price;
    }
}
