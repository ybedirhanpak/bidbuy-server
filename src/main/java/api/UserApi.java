package api;

import api.dto.Message;
import api.dto.UserAuth;
import api.dto.IdHolder;
import api.model.User;
import core.Request;
import core.Response;
import database.DatabaseManager;

public class UserApi {

    private static final DatabaseManager<User> db = new DatabaseManager<>(User.class);

    public static Response getUserList(Request request) {
        return new Response(db.getAll(), 200);
    }

    public static Response getUser(Request request) {
        IdHolder body = (IdHolder) request.body;
        int userId = body.id;
        User user = db.get(userId);
        if (user != null) {
            return new Response(user, 200);
        }
        return new Response(new Message("User cannot be retrieved."), 500);
    }

    public static Response login(Request request) {
        UserAuth body = (UserAuth) request.body;
        User dbUser = db.getWithKeyValue("username", body.username);
        if(dbUser != null && body.password.hashCode() == dbUser.passwordHash) {
            return new Response(dbUser, 200);
        }
        return new Response(new Message("Wrong username or password"), 500);
    }

    public static Response register(Request request) {
        UserAuth body = (UserAuth) request.body;
        User user = new User(body.username, body.password.hashCode());
        User userCreated = db.create(user);
        if (userCreated != null) {
            return new Response(userCreated, 200);
        }

        return new Response(new Message("User cannot be created."), 500);
    }
}
