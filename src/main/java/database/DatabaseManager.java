package database;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager<T extends DatabaseModel> {

    private final Gson gson = new Gson();
    private final String typeName;
    private final Class<T> className;
    private DatabaseType type;

    public DatabaseManager(Class<T> className) {
        this.typeName = className.getSimpleName().toLowerCase();
        this.className = className;
        System.out.println(className.toString());
        try {
            String typeFileName = getTypeFile();
            boolean fileCreated = this.createFile(typeFileName);
            if (fileCreated) {
                // Create type object
                this.type = new DatabaseType(this.typeName);
                // Save type object into file
                writeToFile(type, typeFileName);
            } else {
                // Read type object from file
                this.type = readTypeFromFile(typeFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTypeFile() {
        return "db/" + this.typeName + "/" + this.typeName + "_type.json";
    }

    private String getObjectFile(int id) {
        return "db/" + this.typeName + "/" + this.typeName + "_" + id + ".json";
    }

    private boolean createFile(String fileName) throws IOException {
        File file = new File(fileName);
        boolean dirCreated = file.getParentFile().mkdirs();
        boolean fileCreated = file.createNewFile();
        return dirCreated || fileCreated;
    }

    private boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void writeToFile(Object object, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        gson.toJson(object, writer);
        writer.close();
    }

    private DatabaseType readTypeFromFile(String fileName) throws IOException {
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return gson.fromJson(reader, new TypeToken<DatabaseType>() {
        }.getType());
    }

    private T readObjectFromFile(String fileName) throws IOException {
        JsonReader reader = new JsonReader(new FileReader(fileName));
        return gson.fromJson(reader, className);
    }

    public T get(int id) {
        String fileName = getObjectFile(id);
        File file = new File(fileName);
        if (file.exists()) {
            try {
                return readObjectFromFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public T getWithKeyValue(String key, Object value) {
        for (int i = 1; i <= type.getTypeId(); i++) {
            String fileName = getObjectFile(i);
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    JsonReader reader = new JsonReader(new FileReader(fileName));
                    LinkedTreeMap<String, Object> map =  gson.fromJson(reader, LinkedTreeMap.class);
                    if(map.get(key).equals(value)) {
                        return get(i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<T> getAllWithKeyValue(String key, Object value) {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 1; i <= type.getTypeId(); i++) {
            String fileName = getObjectFile(i);
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    JsonReader reader = new JsonReader(new FileReader(fileName));
                    LinkedTreeMap<String, Object> map =  gson.fromJson(reader, LinkedTreeMap.class);
                    if(map.get(key).equals(value)) {
                        result.add(get(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public T create(T obj) {
        int id = type.getTypeId();
        String fileName = getObjectFile(id);
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                // Set object id and save into file
                obj.id = id;
                writeToFile(obj, fileName);
                // Increase id and save into file
                type.increaseTypeId();
                writeToFile(type, getTypeFile());
                return obj;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<T> getAll() {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 1; i <= type.getTypeId(); i++) {
            T obj = get(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    public T update(T obj) {
        T old = get(obj.id);
        if (old != null) {
            try {
                // Update the file directly
                writeToFile(obj, getObjectFile(obj.id));
                return obj;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean delete(T obj) {
        T old = get(obj.id);
        if (old != null) {
            // Delete the file directly
            return deleteFile(getObjectFile(obj.id));
        }
        return false;
    }
}
