package api.dto;

public class UserAuth {
    public String username;
    public String password;

    public UserAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
