package api.dto;

public class ProductCreate {
    public String name;
    public double price;
    public int ownerId;

    public ProductCreate(String name, double price, int ownerId) {
        this.name = name;
        this.price = price;
        this.ownerId = ownerId;
    }
}
