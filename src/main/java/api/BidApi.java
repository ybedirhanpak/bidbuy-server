package api;

import api.dto.*;
import api.model.Bid;
import api.model.Product;
import core.Request;
import core.Response;
import database.DatabaseManager;

import java.util.List;

public class BidApi {

    private static final DatabaseManager<Bid> db = new DatabaseManager<>(Bid.class);
    private static final DatabaseManager<Product> product_db = new DatabaseManager<>(Product.class);


    public static Response getBidList(Request request) {
        return new Response(db.getAll(), 200);
    }

    public static Response getBidListOfProduct(Request request) {
        ProductIdHolder body = (ProductIdHolder) request.body;
        List<Bid> bidList = db.getAllWithKeyValue("toProductId", (double) body.productId);
        return new Response(bidList, 200);
    }

    public static Response getBidListOfUser(Request request) {
        UserIdHolder body = (UserIdHolder) request.body;
        List<Bid> bidList = db.getAllWithKeyValue("fromUserId", (double) body.userId);
        return new Response(bidList, 200);
    }

    public static Response getBid(Request request) {
        IdHolder body = (IdHolder) request.body;
        int bidId = body.id;
        Bid bid = db.get(bidId);
        if (bid != null) {
            return new Response(bid, 200);
        }
        return new Response(new Message("Bid cannot be retrieved."), 500);
    }

    public static Response createBid(Request request) {
        BidCreate body = (BidCreate) request.body;
        Bid bid = new Bid(body.fromUserId, body.toProductId, body.price);
        Product product = product_db.get(body.toProductId);
        if (body.price <= product.price) {
            return new Response(
                    new Message("Bid price must be greater than product price"),
                    500);
        }

        // Create bid
        Bid bidCreated = db.create(bid);
        if (bidCreated == null) {
            return new Response(new Message("Bid cannot be created."), 500);
        }

        // Update product
        product.price = body.price;
        product.lastBidId = bidCreated.id;
        Product updatedProduct = product_db.update(product);
        if (updatedProduct == null) {
            db.delete(bidCreated);
            return new Response(
                    new Message("Bid cannot be created: Error while updating the product"),
                    500);
        }

        return new Response(bidCreated, 200);
    }

    public static Response updateBid(Request request) {
        Bid body = (Bid) request.body;
        Bid updatedBid = db.update(body);
        if (updatedBid != null) {
            return new Response(updatedBid, 200);
        }

        return new Response(new Message("Bid cannot be updated."), 500);
    }

    public static Response deleteBid(Request request) {
        Bid body = (Bid) request.body;
        boolean deleted = db.delete(body);
        if (deleted) {
            return new Response(new Message("Bid is deleted"), 200);
        }

        return new Response(new Message("Bid cannot be deleted."), 500);
    }
}
