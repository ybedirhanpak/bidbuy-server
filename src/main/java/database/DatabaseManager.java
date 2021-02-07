package database;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import api.dto.IdHolder;
import concurrency.Lock;
import concurrency.LockManager;
import core.ThreadManager;
import core.Util;

public class DatabaseManager<T extends DatabaseModel> {

    // Unique variables to this database type
    private final String typeName;
    private final Class<T> className;
    private DatabaseType type;

    public DatabaseManager(Class<T> className) {
        this.typeName = className.getSimpleName().toLowerCase();
        this.className = className;
        try {
            String typeFileName = getTypeJSONFileName();
            boolean fileCreated = Util.createFile(typeFileName);
            if (fileCreated) {
                // Create type object
                this.type = new DatabaseType(this.typeName);
                // Save type object into file
                Util.writeObjectToJSONFile(type, typeFileName);
            } else {
                // Read type object from file
                this.type = Util.readObjectFromJSONFile(typeFileName, DatabaseType.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized Lock getLock(int id) {
        return LockManager.getLock(className, id);
    }

    private String getTypeJSONFileName() {
        return "db/" + this.typeName + "/" + this.typeName + "_type.json";
    }

    private String getObjectJSONFileName(int id) {
        return "db/" + this.typeName + "/" + this.typeName + "_" + id + ".json";
    }

    private void printLockMessage(int id, String message) {
        getLock(id).printMsg(message  + " : " + className.getSimpleName());
    }

    public T get(int id) {
        printLockMessage(id, "Before get");
        T obj = null;
        try {
            getLock(id).lock();
            printLockMessage(id, "Inside get");
            String fileName = getObjectJSONFileName(id);
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    obj = Util.readObjectFromJSONFile(fileName, className);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            getLock(id).release();
        }
        return obj;
    }

    public T getWithKeyValue(String key, Object value) {
        Gson gson = Util.getGson();
        for (int i = 0; i < type.getTypeId(); i++) {
            String fileName = getObjectJSONFileName(i);
            try {
                getLock(i).lock();
                File file = new File(fileName);
                if (file.exists()) {
                    try {
                        JsonReader reader = new JsonReader(new FileReader(fileName));
                        LinkedTreeMap<String, Object> map = gson.fromJson(reader, LinkedTreeMap.class);
                        if (map.get(key).equals(value)) {
                            return get(i);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                getLock(i).release();
            }
        }
        return null;
    }

    public List<T> getAllWithKeyValue(String key, Object value) {
        Gson gson = Util.getGson();
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < type.getTypeId(); i++) {
            String fileName = getObjectJSONFileName(i);
            try {
                getLock(i).lock();
                File file = new File(fileName);
                if (file.exists()) {
                    try {
                        JsonReader reader = new JsonReader(new FileReader(fileName));
                        LinkedTreeMap<String, Object> map = gson.fromJson(reader, LinkedTreeMap.class);
                        if (map.get(key).equals(value)) {
                            T obj = get(i);
                            if (obj != null) {
                                result.add(obj);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                getLock(i).release();
            }
        }
        return result;
    }

    public T create(T obj) {
        ThreadManager.message("Before create");
        int id = type.getCurrentIdAndIncrease();
        // If increase is unsuccessful, return
        if (id < 0) {
            return null;
        }
        try {
            getLock(id).lock();
            printLockMessage(id, "Inside create");
            String fileName = getObjectJSONFileName(id);
            File file = new File(fileName);
            // If there is an object with the same id, don't create a new one
            if (!file.exists()) {
                try {
                    // Set object id and save into file
                    obj.id = id;
                    Util.writeObjectToJSONFile(obj, fileName);
                    return obj;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            getLock(id).release();
        }
        return null;
    }

    public List<T> getAll() {
        ThreadManager.message("getAll");
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
        printLockMessage(obj.id, "Before update");
        try {
            getLock(obj.id).lock();
            printLockMessage(obj.id, "Inside update");
            T old = get(obj.id);
            if (old != null) {
                try {
                    // Update the file directly
                    Util.writeObjectToJSONFile(obj, getObjectJSONFileName(obj.id));
                    return obj;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            getLock(obj.id).release();
        }
        return null;
    }

    public boolean delete(IdHolder obj) {
        printLockMessage(obj.id, "Before delete");
        boolean deleted = false;
        try {
            getLock(obj.id).lock();
            printLockMessage(obj.id, "Inside delete");
            T old = get(obj.id);
            if (old != null) {
                deleted = Util.deleteFile(getObjectJSONFileName(obj.id));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            getLock(obj.id).release();
        }
        return deleted;
    }
}
