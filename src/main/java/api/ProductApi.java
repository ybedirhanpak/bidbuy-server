package api;

import api.dto.Retrieve;
import api.dto.ProductCreate;
import core.Request;
import core.Response;
import database.DatabaseManager;
import api.model.Product;

public class ProductApi {

    private static final DatabaseManager<Product> db = new DatabaseManager<>(Product.class);

    public static Response getProductList(Request request) {
       return new Response(db.getAll());
    }

    public static Response getProduct(Request request) {
        Retrieve body = (Retrieve) request.body;
        int productId = body.id;
        Product product = db.get(productId);
        return new Response(product);
    }

    public static Response createProduct(Request request) {
        ProductCreate body = (ProductCreate) request.body;
        Product product = new Product(body.name, body.price, body.ownerId, body.imageURL);
        Product productCreated = db.create(product);
        return new Response(productCreated);
    }
}
