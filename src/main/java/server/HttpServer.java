package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private final int port;
    private final Router router;

    public HttpServer(int port, Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                Thread.startVirtualThread(() -> {
                    System.out.println("Accepting new connection");
                    new ClientHandler(clientSocket, router).run();
                });
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error starting server", e);
        }
    }
}
