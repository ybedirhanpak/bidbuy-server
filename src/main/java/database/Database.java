package database;

import api.model.Bid;
import api.model.Product;
import api.model.User;

public class Database {
    public static final DatabaseManager<Product> product = new DatabaseManager<>(Product.class);
    public static final DatabaseManager<User> user = new DatabaseManager<>(User.class);
    public static final DatabaseManager<Bid> bid = new DatabaseManager<>(Bid.class);
}
