package api;

import api.dto.Message;
import api.dto.UserAuth;
import api.dto.IdHolder;
import api.dto.UserOut;
import api.model.User;
import core.Request;
import core.Response;
import database.Database;

import java.util.List;
import java.util.stream.Collectors;

public class UserApi {

    public static Response getUserList(Request request) {
        List<User> userList = Database.user.getAll();
        List<UserOut> userOutList = userList.stream().map(UserOut::new).collect(Collectors.toList());
        return new Response(userOutList, 200);
    }

    public static Response getUser(Request request) {
        IdHolder body = (IdHolder) request.body;
        int userId = body.id;
        User user = Database.user.get(userId);
        if (user != null) {
            return new Response(new UserOut(user), 200);
        }
        return new Response(new Message("User cannot be retrieved."), 500);
    }

    public static Response login(Request request) {
        UserAuth body = (UserAuth) request.body;
        User dbUser = Database.user.getWithKeyValue("username", body.username);

        if (dbUser == null) {
            return new Response(new Message("User not found"), 500);
        }

        if (body.password.hashCode() == dbUser.passwordHash) {
            return new Response(new UserOut(dbUser), 200);
        }

        return new Response(new Message("Wrong username or password"), 500);
    }

    public static Response register(Request request) {
        UserAuth body = (UserAuth) request.body;
        if (body.username.equals("")) {
            return new Response(new Message("Field cannot be empty: username"), 500);
        }
        if (body.password.equals("")) {
            return new Response(new Message("Field cannot be empty: password"), 500);
        }
        // Check if a user exists with requested username
        User userOld = Database.user.getWithKeyValue("username", body.username);
        if (userOld != null) {
            return new Response(new Message("User with " + body.username + " already exists."), 500);
        }
        // Create user
        User user = new User(body.username, body.password.hashCode());
        User userCreated = Database.user.create(user);
        if (userCreated != null) {
            return new Response(new UserOut(userCreated), 200);
        }
        return new Response(new Message("User cannot be created."), 500);
    }

    public static Response deleteUser(Request request) {
        IdHolder body = (IdHolder) request.body;
        boolean deleted = Database.user.delete(body);
        if (deleted) {
            return new Response(new Message("User" + body.id + " is deleted"), 200);
        }
        return new Response(new Message("User" + body.id + " cannot be deleted."), 500);
    }
}
