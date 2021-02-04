package api;

import api.dto.*;
import api.model.Bid;
import api.model.User;
import core.Request;
import core.Response;
import database.DatabaseManager;
import api.model.Product;

public class ProductApi {

    private static final DatabaseManager<Product> db = new DatabaseManager<>(Product.class);
    private static final DatabaseManager<User> userDb = new DatabaseManager<>(User.class);
    private static final DatabaseManager<Bid> bidDb = new DatabaseManager<>(Bid.class);


    public static Response getProductList(Request request) {
        return new Response(db.getAll(), 200);
    }

    public static Response getProduct(Request request) {
        IdHolder body = (IdHolder) request.body;
        int productId = body.id;
        Product product = db.get(productId);
        if (product != null) {
            User owner = userDb.get(product.ownerId);
            Bid lastBid = bidDb.get(product.lastBidId);
            User fromUser = userDb.get(lastBid.fromUserId);
            ProductOut productOut = new ProductOut(
                    product,
                    new UserOut(owner),
                    new BidOut(lastBid, new UserOut(fromUser))
            );
            return new Response(productOut, 200);
        }
        return new Response(new Message("Product cannot be retrieved."), 500);
    }

    public static Response createProduct(Request request) {
        ProductCreate body = (ProductCreate) request.body;
        Product product = new Product(body.name, body.price, body.ownerId, body.imageURL);
        Product productCreated = db.create(product);
        if (productCreated != null) {
            return new Response(productCreated, 200);
        }

        return new Response(new Message("Product cannot be created."), 500);
    }

    public static Response updateProduct(Request request) {
        Product body = (Product) request.body;
        Product updatedProduct = db.update(body);
        if (updatedProduct != null) {
            return new Response(updatedProduct, 200);
        }

        return new Response(new Message("Product cannot be updated."), 500);
    }

    public static Response deleteProduct(Request request) {
        Product body = (Product) request.body;
        boolean deleted = db.delete(body);
        if (deleted) {
            return new Response(new Message("Product is deleted"), 200);
        }

        return new Response(new Message("Product cannot be deleted."), 500);
    }
}
