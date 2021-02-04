package core.route;

import api.BidApi;
import api.ProductApi;
import api.UserApi;
import api.dto.*;
import api.model.Bid;
import api.model.Product;
import com.google.gson.Gson;
import core.Request;
import core.Response;

public class Router {

    private final static Gson gson = new Gson();

    private static void convertRequestBody(Request request, Class<?> className) {
        request.body = gson.fromJson(gson.toJsonTree(request.body).getAsJsonObject(), className);
    }

    public static Response routeRequest(Request request) {
        Response response = new Response();
        switch (request.identifier) {
            case "getProductList":
                response = ProductApi.getProductList(request);
                break;
            case "getProduct":
                convertRequestBody(request, IdHolder.class);
                response = ProductApi.getProduct(request);
                break;
            case "createProduct":
                convertRequestBody(request, ProductCreate.class);
                response = ProductApi.createProduct(request);
                break;
            case "updateProduct":
                convertRequestBody(request, Product.class);
                response = ProductApi.updateProduct(request);
                break;
            case "deleteProduct":
                convertRequestBody(request, Product.class);
                response = ProductApi.deleteProduct(request);
                break;
            case "getUserList":
                response = UserApi.getUserList(request);
                break;
            case "getUser":
                convertRequestBody(request, IdHolder.class);
                response = UserApi.getUser(request);
                break;
            case "register":
                convertRequestBody(request, UserAuth.class);
                response = UserApi.register(request);
                break;
            case "login":
                convertRequestBody(request, UserAuth.class);
                response = UserApi.login(request);
                break;
            case "getBidList":
                response = BidApi.getBidList(request);
                break;
            case "getBidListOfProduct":
                convertRequestBody(request, ProductIdHolder.class);
                response = BidApi.getBidListOfProduct(request);
                break;
            case "getBidListOfUser":
                convertRequestBody(request, UserIdHolder.class);
                response = BidApi.getBidListOfUser(request);
                break;
            case "getBid":
                convertRequestBody(request, IdHolder.class);
                response = BidApi.getBid(request);
                break;
            case "createBid":
                convertRequestBody(request, BidCreate.class);
                BidCreate body = (BidCreate) request.body;
                response = BidApi.createBid(request);
                SubscriptionManager.triggerSubscriptions("createBid", body.toProductId);
                break;
            case "updateBid":
                convertRequestBody(request, Bid.class);
                response = BidApi.updateBid(request);
                break;
            case "deleteBid":
                convertRequestBody(request, Bid.class);
                response = BidApi.deleteBid(request);
                break;
            default:
                break;
        }
        return response;
    }
}
