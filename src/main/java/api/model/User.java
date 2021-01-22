package api.model;

import database.DatabaseModel;

public class User extends DatabaseModel {
    public String username;
    public int passwordHash;

    public User(String username, int passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
