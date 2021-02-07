package api;

import api.dto.*;
import api.model.Bid;
import api.model.Product;
import api.model.User;
import core.Request;
import core.Response;
import database.Database;

import java.util.ArrayList;
import java.util.List;

public class BidApi {

    public static Response getBidList(Request request) {
        return new Response(Database.bid.getAll(), 200);
    }

    public static Response getBidListOfProduct(Request request) {
        ProductIdHolder body = (ProductIdHolder) request.body;
        List<Bid> bidList = Database.bid.getAllWithKeyValue("toProductId", (double) body.productId);
        List<BidOut> bidOutList = new ArrayList<>();
        bidList.forEach(bid -> {
            User fromUser = Database.user.get(bid.fromUserId);
            bidOutList.add(new BidOut(bid, new UserOut(fromUser)));
        });
        return new Response(bidOutList, 200);
    }

    public static Response getBidListOfUser(Request request) {
        UserIdHolder body = (UserIdHolder) request.body;
        List<Bid> bidList = Database.bid.getAllWithKeyValue("fromUserId", (double) body.userId);
        return new Response(bidList, 200);
    }

    public static Response getBid(Request request) {
        IdHolder body = (IdHolder) request.body;
        int bidId = body.id;
        Bid bid = Database.bid.get(bidId);
        if (bid != null) {
            return new Response(bid, 200);
        }
        return new Response(new Message("Bid cannot be retrieved."), 500);
    }

    public static synchronized Response createBid(Request request) {
        BidCreate body = (BidCreate) request.body;
        Bid bid = new Bid(body.fromUserId, body.toProductId, body.price);
        Product product = Database.product.get(body.toProductId);
        if (body.price <= product.price) {
            return new Response(
                    new Message("Bid price must be greater than product price"),
                    500);
        }

        if(body.fromUserId == product.ownerId) {
            return new Response(
                    new Message("Product owners cannot give bid."),
                    500);
        }

        // Create bid
        Bid bidCreated = Database.bid.create(bid);
        if (bidCreated == null) {
            return new Response(new Message("Bid cannot be created."), 500);
        }

        // Update product
        product.price = body.price;
        product.lastBidId = bidCreated.id;
        Product updatedProduct = Database.product.update(product);
        if (updatedProduct == null) {
            Database.bid.delete(new IdHolder(bidCreated.id));
            return new Response(
                    new Message("Bid cannot be created: Error while updating the product"),
                    500);
        }

        return new Response(bidCreated, 200);
    }

    public static Response deleteBid(Request request) {
        IdHolder body = (IdHolder) request.body;
        boolean deleted = Database.bid.delete(body);
        if (deleted) {
            return new Response(new Message("Bid is deleted"), 200);
        }

        return new Response(new Message("Bid cannot be deleted."), 500);
    }
}
