package core;

import api.ProductApi;
import api.UserApi;
import api.dto.Retrieve;
import api.dto.ProductCreate;
import api.dto.UserAuth;
import api.model.Product;
import com.google.gson.Gson;

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
                convertRequestBody(request, Retrieve.class);
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
                convertRequestBody(request, Retrieve.class);
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
            default:
                break;
        }
        return response;
    }
}
