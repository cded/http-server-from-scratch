package middleware;

import model.Response;
import server.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.GZIPOutputStream;

public class Gzip {
    public static Function<Handler, Handler> withGzip() {
        return handler -> req -> {
            Response res = handler.handle(req);
            String ae = req.headers().getOrDefault("accept-encoding", "");
            if (!ae.contains("gzip") || res.body() == null || res.body().length == 0)
                return res;

            try {
                byte[] compressed = gzipCompress(res.body());
                Map<String, String> headers = new HashMap<>(res.headers());
                headers.put("Content-Encoding", "gzip");
                headers.put("Content-Length", String.valueOf(compressed.length));
                return new Response(res.statusCode(), res.statusMessage(), headers, compressed);
            } catch (IOException e) {
                return Response.internalServerError("Failed to gzip");
            }
        };
    }

    private static byte[] gzipCompress(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(data);
        }
        return out.toByteArray();
    }
}
