package core;

import api.ProductApi;
import api.dto.Retrieve;
import api.dto.ProductCreate;
import com.google.gson.Gson;

public class Router {

    private final static Gson gson = new Gson();

    private static void convertRequestBody(Request request, Class<?> className) {
        request.body = gson.fromJson(gson.toJsonTree(request.body).getAsJsonObject(), className);
    }

    public static Response routeRequest(Request request) {
        Response response = new Response(null);
        switch (request.identifier) {
            case "getProductList":
                response = ProductApi.getProductList(request);
                break;
            case "getProduct":
                convertRequestBody(request, Retrieve.class);
                response = ProductApi.getProduct(request);
                break;
            case "createProduct":
                convertRequestBody(request, ProductCreate.class);
                response = ProductApi.createProduct(request);
                break;
            default:
                break;
        }
        return response;
    }
}
