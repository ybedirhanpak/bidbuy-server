package database;

public class DatabaseType {
    private final String typeName;
    private int id;

    public DatabaseType(String typeName) {
        this.typeName = typeName;
        this.id = 1;
    }

    public String getFileName() {
        return "db/" + this.typeName + "/" + this.typeName + "_type.json";
    }

    public int getTypeId() {
        return id;
    }

    public void increaseTypeId() {
        this.id++;
    }
}
