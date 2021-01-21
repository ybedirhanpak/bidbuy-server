package api;

import api.dto.Retrieve;
import api.dto.ProductCreate;
import core.Request;
import core.Response;
import database.DatabaseManager;
import database.model.Product;

public class ProductApi {

    private static final DatabaseManager db = new DatabaseManager();

    public static Response getProductList(Request request) {
       return new Response(db.getProductList());
    }

    public static Response getProduct(Request request) {
        Retrieve body = (Retrieve) request.body;
        int productId = body.id;
        Product product = db.getProduct(productId);
        return new Response(product);
    }

    public static Response createProduct(Request request) {
        ProductCreate body = (ProductCreate) request.body;
        Product product = db.createProduct(
                body.name,
                body.price,
                body.ownerId,
                body.imageURL
        );
        return new Response(product);
    }
}
