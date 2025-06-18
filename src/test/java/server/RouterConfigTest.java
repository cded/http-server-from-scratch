package server;

import model.Request;
import model.Response;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RouterConfigTest {
    @Test
    public void testBaseRoutes() {
        RouterConfig rc = new RouterConfig(".");
        Response res = rc.getRouter().find("GET", "/").handle(new Request("GET", "/", "HTTP/1.1", Map.of(), ""));
        assertEquals("Welcome", new String(res.body()));

        Response ua = rc.getRouter().find("GET", "/user-agent").handle(new Request("GET", "/user-agent", "HTTP/1.1", Map.of("user-agent", "UA"), ""));
        assertEquals("UA", new String(ua.body()));
    }

    @Test
    public void testFileRoutes() throws Exception {
        Path tmp = Files.createTempDirectory("dir");
        RouterConfig rc = new RouterConfig(tmp.toString());
        String path = tmp.resolve("a.txt").toString();
        Response post = rc.getRouter().find("POST", "/files/a.txt").handle(new Request("POST", "/files/a.txt", "HTTP/1.1", Map.of("content-length", "3"), "abc"));
        assertEquals(201, post.statusCode());
        assertTrue(Files.exists(Path.of(path)));

        Response get = rc.getRouter().find("GET", "/files/a.txt").handle(new Request("GET", "/files/a.txt", "HTTP/1.1", Map.of(), ""));
        assertEquals(200, get.statusCode());
        assertEquals("application/octet-stream", get.headers().get("Content-Type"));
        assertEquals("abc", new String(get.body()));
    }
}