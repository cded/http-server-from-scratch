package utils;

import model.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class RequestParser {

    public static Request parse(BufferedReader br) throws IOException {
        String requestLine = getRequestLine(br);
        if (requestLine == null || requestLine.trim().isEmpty()) {
            return null; // signal end of connection
        }

        String[] parts = requestLine.split(" ");
        if (parts.length < 3) throw new IOException("Malformed request line");

        String method = parts[0];
        String path = parts[1];
        String httpVersion = parts[2];

        HashMap<String, String> requestHeaders = getRequestHeaders(br);
        int bodyLength = Integer.parseInt(requestHeaders.getOrDefault("content-length", "0"));
        String requestBody = getRequestBody(br, bodyLength);

        return new Request(method, path, httpVersion, requestHeaders, requestBody);
    }

    public static String getRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if(line == null || line.isEmpty()) {
            return null;
        }
        else {
            return line;
        }
    }

    public static HashMap<String, String> getRequestHeaders(BufferedReader br) throws IOException {
        HashMap<String, String> requestHeaders = new HashMap<>();
        String line;
        while((line = br.readLine()) != null && !line.isEmpty()) {
            System.out.println(line);
            int i = line.indexOf(":");
            if(i == 0) {
                throw new IOException("Malformed request header");
            }
            String headerName = line.substring(0, i).toLowerCase();
            String headerValue = line.substring(i + 1).trim();
            requestHeaders.put(headerName, headerValue);
        }
        return requestHeaders;
    }

    public static String getRequestBody(BufferedReader br, int bodyLength) throws IOException {
        if(bodyLength == 0) {
            return "";
        }
        char[] body = new char[bodyLength];
        br.read(body, 0, bodyLength);
        return new String(body);
    }
}
