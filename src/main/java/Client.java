import core.Request;
import core.Response;
import com.google.gson.Gson;
import core.Util;

import java.io.*;
import java.net.Socket;

public class Client {

    private final Gson gson = new Gson();

    public void sendRequest(String identifier, Object body) {
        Request request = new Request(identifier, body);

        try {
            Socket clientSocket = new Socket("192.168.1.67", 9000);

            // Send request
            String requestJson = gson.toJson(request);
            Util.writeJsonToOutputStream(requestJson, clientSocket.getOutputStream());

            // Get response
            String responseJson = Util.inputStreamToJson(clientSocket.getInputStream());
            System.out.println("Response received by client " + responseJson);

            Response response = gson.fromJson(responseJson, Response.class);
            System.out.println("Response received by client " + response.body);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
