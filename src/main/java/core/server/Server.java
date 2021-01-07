package core.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final int port;
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        createServerSocket();
        while (!isStopped) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                handleClientSocket(clientSocket);
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Error occurred while accepting connection because server is stopped.");
                    e.printStackTrace();
                    return;
                }
                throw new RuntimeException("Error occurred while accepting connection", e);
            }
        }
    }

    private void createServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Server couldn't be opened on port" + this.port, e);
        }
    }

    private boolean isStopped() {
        return isStopped;
    }

    private void handleClientSocket(Socket clientSocket) {
        Thread requestHandlerThread = new Thread(new ServerWorker(clientSocket));
        requestHandlerThread.start();
    }

    public void stopServer() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while closing the server", e);
        }
    }
}
