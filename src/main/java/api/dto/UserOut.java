package api.dto;

import api.model.User;

public class UserOut {
    public int id;
    public String username;

    public UserOut(User user) {
        this.id = user.id;
        this.username = user.username;
    }
}
