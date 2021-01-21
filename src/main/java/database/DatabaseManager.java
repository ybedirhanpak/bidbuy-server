package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import database.model.Product;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final Gson gson = new Gson();

    private final List<Product> productList;
    private final String FILE_NAME = "db/products.json";
    private final Type LIST_TYPE = new TypeToken<List<Product>>() {}.getType();

    private int productID;

    public DatabaseManager() {
        this.productList = readFromFile();
        this.productID = productList.size();
    }

    private void checkFileExists() throws IOException {
        File file = new File(FILE_NAME);
        boolean dirCreated = file.getParentFile().mkdirs();
        boolean fileCreated = file.createNewFile();
    }

    private void saveToFile() {
        try {
            checkFileExists();
            FileWriter writer = new FileWriter(FILE_NAME);
            gson.toJson(this.productList, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Product> readFromFile() {
        try {
            checkFileExists();
            JsonReader reader = new JsonReader(new FileReader(FILE_NAME));
            ArrayList<Product> list = gson.fromJson(reader, LIST_TYPE);
            if (list == null) {
                list = new ArrayList<>();
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Product getProduct(int id) {
        return productList.stream()
                .filter(p -> p.id == id)
                .findFirst()
                .orElse(null);
    }

    public Product createProduct(String name, double price, int ownerId, String imageURL) {
        Product product = new Product(this.productID, name, price, ownerId, imageURL);
        productList.add(product);
        this.productID++;

        saveToFile();
        return product;
    }

    public Product updateProduct(Product _product) {
        Product product = getProduct(_product.id);

        if (product != null) {
            product.copyFrom(_product);
        }

        saveToFile();
        return product;
    }

    public void removeProduct(Product _product) {
        Product product = getProduct(_product.id);

        if (product != null) {
            productList.remove(product);
        }

        saveToFile();
    }
}
