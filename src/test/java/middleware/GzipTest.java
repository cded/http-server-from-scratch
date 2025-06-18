package middleware;

import model.Request;
import model.Response;
import org.junit.jupiter.api.Test;
import server.Handler;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GzipTest {
    @Test
    public void testGzipAppliedWhenAccepted() {
        Handler h = req -> Response.ok("text");
        Handler gz = Gzip.withGzip().apply(h);
        Request request = new Request("GET", "/", "HTTP/1.1", Map.of("accept-encoding", "gzip"), "");
        Response res = gz.handle(request);
        assertEquals("gzip", res.headers().get("Content-Encoding"));
        assertNotEquals(res.body().length, "text".getBytes().length);
    }

    @Test
    public void testNoGzipWhenNotAccepted() {
        Handler h = req -> Response.ok("text");
        Handler gz = Gzip.withGzip().apply(h);
        Request request = new Request("GET", "/", "HTTP/1.1", Map.of(), "");
        Response res = gz.handle(request);
        assertNull(res.headers().get("Content-Encoding"));
        assertEquals("text", new String(res.body()));
    }
}