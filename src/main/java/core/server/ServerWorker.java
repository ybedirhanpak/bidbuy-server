package core.server;

import core.Request;
import core.Response;
import com.google.gson.Gson;
import core.Router;
import core.Util;

import java.io.*;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private final Gson gson = new Gson();
    private final Socket clientSocket;

    public ServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Connection from " +
                clientSocket.getRemoteSocketAddress()
        );
        try {
            // Get request
            String requestJson =  Util.inputStreamToJson(clientSocket.getInputStream());
            Request request = gson.fromJson(requestJson, Request.class);

            // Handle request
            System.out.println("Server thread received: " + request.identifier);
            Response response = Router.routeRequest(request);

            // Send response
            String responseJson = gson.toJson(response);
            Util.writeJsonToOutputStream(responseJson, clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error occurred while handling request.");
            e.printStackTrace();
        }
    }
}
