package server;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import model.Request;
import model.Response;
import utils.RequestParser;

import java.io.*;
import java.net.Socket;

//@RequiredArgsConstructor
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private final Router router;

    public ClientHandler(Socket clientSocket, Router router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        try(InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            Boolean keepAlive = true;

            while(keepAlive) {
                Request request = RequestParser.parse(br);
                if (request == null) {
                    break;
                }

                keepAlive = !request.headers().getOrDefault("connection", "").equals("close");

                String method = request.method();
                String path = request.path();

                Response response = router.find(method, path).handle(request);

                if (!keepAlive) {
                    response.headers().put("Connection", "close");
                }

                response.writeTo(outputStream);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error reading client request" + e);
        }
    }
}
