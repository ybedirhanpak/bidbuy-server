package api;

import core.Request;
import core.Response;
import database.DatabaseManager;

public class Product {

    private static final DatabaseManager db = new DatabaseManager();

    public static Response increaseValue(Request request) {
        db.increasePriceValue();
        return new Response(db.getPriceValue());
    }

    public static Response getValue(Request request) {
        return new Response(db.getPriceValue());
    }
}
