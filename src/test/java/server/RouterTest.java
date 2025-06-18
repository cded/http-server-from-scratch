package server;

import model.Request;
import model.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {
    @Test
    public void testAddAndFindExactRoute() {
        Router router = new Router();
        router.add("GET", "/hello", req -> Response.ok("hi"));
        Handler h = router.find("GET", "/hello");
        Response res = h.handle(new Request("GET", "/hello", "HTTP/1.1", Map.of(), ""));
        assertEquals(200, res.statusCode());
        assertEquals("hi", new String(res.body()));
    }

    @Test
    public void testWildcardRoute() {
        Router router = new Router();
        router.add("GET", "/echo/*", req -> Response.ok("echo"));
        Handler h = router.find("GET", "/echo/test");
        assertEquals("echo", new String(h.handle(new Request("GET", "/echo/test", "HTTP/1.1", Map.of(), "")).body()));
    }

    @Test
    public void testDefaultHandler() {
        Router router = new Router();
        Handler h = router.find("GET", "/missing");
        Response res = h.handle(new Request("GET", "/missing", "HTTP/1.1", Map.of(), ""));
        assertEquals(404, res.statusCode());
    }

    @Test
    public void testMiddlewareApplied() {
        Router router = new Router();
        router.use(next -> req -> {
            Response r = next.handle(req);
            return Response.customMethod(r.statusCode(), r.statusMessage(), new String(r.body()) + "-mw", Map.of());
        });
        router.add("GET", "/test", req -> Response.ok("orig"));
        Response res = router.find("GET", "/test").handle(new Request("GET", "/test", "HTTP/1.1", Map.of(), ""));
        assertEquals("orig-mw", new String(res.body()));
    }
}