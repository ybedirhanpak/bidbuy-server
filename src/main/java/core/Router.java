package core;

import api.Product;

public class Router {

    public static Response routeRequest(Request request) {
        Response response = null;
        switch (request.identifier) {
            case "increaseValue":
                response = Product.increaseValue(request);
                break;
            case "getValue":
                response = Product.getValue(request);
                break;
            default:
                break;
        }
        return response;
    }
}
