package core.server;

import core.*;
import com.google.gson.Gson;
import core.route.Router;
import core.route.SubscriptionManager;

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
            String requestJson = Util.inputStreamToJson(clientSocket.getInputStream());
            Request request = gson.fromJson(requestJson, Request.class);
            ThreadManager.message("Server:" + request.identifier + " : " + requestJson);

            // Get a single response
            Response response = Router.routeRequest(request);

            // Send a single response
            String responseJson = gson.toJson(response);
            Util.writeJsonToOutputStream(responseJson, clientSocket.getOutputStream());

            if (request.subscriptionSubject > -1) {
                DataOutputStream requestStream = new DataOutputStream(clientSocket.getOutputStream());
                // Save subscription into map
                Subscription subscription = new Subscription(request, requestStream, request.subscriptionSubject);
                SubscriptionManager.saveSubscription(subscription);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while handling request.");
            e.printStackTrace();
        }
    }
}
