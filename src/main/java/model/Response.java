package model;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public record Response(int statusCode,
                       String statusMessage,
                       Map<String, String> headers,
                       byte[] body) {

    public static Response ok(String bodyText) {
        return constructResponse(200, "OK", bodyText);
    }

    public static Response notFound(String message) {
        return constructResponse(404, "Not Found", message);
    }

    public static Response created(String bodyText) {
        return constructResponse(201, "Created", bodyText);
    }

    public static Response internalServerError(String bodyText) {
        return constructResponse(500, "Internal Server Error", bodyText);
    }

    public static Response customMethod(int statusCode, String statusMessage, String body, Map<String, String> extraHeaders) {
        return constructResponse(statusCode, statusMessage, body, extraHeaders);
    }

    private static Response constructResponse(int statusCode, String statusMessage, String body) {
        return constructResponse(statusCode, statusMessage, body, null);
    }

    private static Response constructResponse(int statusCode, String statusMessage, String body, Map<String, String> extraHeaders) {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "text/plain");
        headers.put("Content-Length", String.valueOf(bodyBytes.length));

        if(extraHeaders != null) {
            headers.putAll(extraHeaders);
        }

        return new Response(statusCode, statusMessage, headers, bodyBytes);
    }

    public void writeTo(OutputStream os) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");
        for (var entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        os.write(body);
        os.flush();
    }
}
