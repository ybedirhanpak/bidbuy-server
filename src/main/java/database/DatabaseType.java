package database;

import core.Util;

import java.io.IOException;

public class DatabaseType {
    private final String typeName;
    private int currentId;

    public DatabaseType(String typeName) {
        this.typeName = typeName;
        this.currentId = 0;
    }

    public String getFileName() {
        return "db/" + this.typeName + "/" + this.typeName + "_type.json";
    }

    public synchronized int getTypeId() {
        System.out.println(Thread.currentThread() + ", gets type id: " + currentId);
        return currentId;
    }

    /**
     * Increases current ID of this type and saves into type file
     * Returns -1 if an error occurs
     * @return increased id
     */
    public synchronized int getCurrentIdAndIncrease() {
        int result = -1;
        try {
            System.out.println("Thread: " + Thread.currentThread() + ", gets type id: " + currentId);
            result = currentId++;
            Util.writeObjectToJSONFile(this, getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public synchronized void increaseTypeId() {
        this.currentId++;
    }
}
