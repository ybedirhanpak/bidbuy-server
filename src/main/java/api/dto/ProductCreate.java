package api.dto;

public class ProductCreate {
    public String name;
    public double price;
    public int ownerId;
    public String imageURL;

    public ProductCreate(String name, double price, int ownerId, String imageURL) {
        this.name = name;
        this.price = price;
        this.ownerId = ownerId;
        this.imageURL = imageURL;
    }
}
