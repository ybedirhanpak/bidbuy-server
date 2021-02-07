package api;

import api.dto.*;
import api.model.Bid;
import api.model.User;
import core.Request;
import core.Response;
import api.model.Product;
import database.Database;

import java.util.List;
import java.util.stream.Collectors;

public class ProductApi {

    private static ProductOut getProductOut(Product product) {
        ProductOut productOut = new ProductOut(product, null, null);
        // Retrieve owner
        User owner = Database.user.get(product.ownerId);
        if (owner != null) {
            productOut.owner = new UserOut(owner);
        }
        // Retrieve last bid
        Bid lastBid = Database.bid.get(product.lastBidId);
        if (lastBid != null) {
            User fromUser = Database.user.get(lastBid.fromUserId);
            productOut.lastBid = new BidOut(lastBid, new UserOut(fromUser));
        }
        return productOut;
    }

    public static Response getProductList(Request request) {
        List<Product> productList = Database.product.getAll();
        List<ProductOut> productOutList = productList.stream()
                .map(ProductApi::getProductOut)
                .collect(Collectors.toList());
        return new Response(productOutList, 200);
    }

    public static Response getProduct(Request request) {
        IdHolder body = (IdHolder) request.body;
        int productId = body.id;
        Product product = Database.product.get(productId);
        if (product != null) {
            ProductOut productOut = getProductOut(product);
            return new Response(productOut, 200);
        }
        return new Response(new Message("Product cannot be retrieved."), 500);
    }

    public static Response createProduct(Request request) {
        ProductCreate body = (ProductCreate) request.body;
        Product product = new Product(body.name, body.price, body.ownerId, body.imageURL);
        Product productCreated = Database.product.create(product);
        if (productCreated != null) {
            return new Response(productCreated, 200);
        }

        return new Response(new Message("Product cannot be created."), 500);
    }

    public static Response updateProduct(Request request) {
        Product body = (Product) request.body;
        Product updatedProduct = Database.product.update(body);
        if (updatedProduct != null) {
            return new Response(updatedProduct, 200);
        }

        return new Response(new Message("Product cannot be updated."), 500);
    }

    public static Response deleteProduct(Request request) {
        IdHolder body = (IdHolder) request.body;
        boolean deleted = Database.product.delete(body);
        if (deleted) {
            return new Response(new Message("Product is deleted"), 200);
        }

        return new Response(new Message("Product cannot be deleted."), 500);
    }
}
