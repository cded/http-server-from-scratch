package utils;

import model.Request;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {
    @Test
    public void testParseSimpleRequest() throws Exception {
        String req = "GET /path HTTP/1.1\r\n" +
                "Content-Length: 5\r\n" +
                "\r\n" +
                "hello";
        BufferedReader br = new BufferedReader(new StringReader(req));
        Request r = RequestParser.parse(br);
        assertEquals("GET", r.method());
        assertEquals("/path", r.path());
        assertEquals("HTTP/1.1", r.httpVersion());
        assertEquals("5", r.headers().get("content-length"));
        assertEquals("hello", r.body());
    }

    @Test
    public void testParseEndsOnEmpty() throws Exception {
        BufferedReader br = new BufferedReader(new StringReader("\r\n"));
        assertNull(RequestParser.parse(br));
    }

    @Test
    public void testMalformedHeaderThrows() {
        String req = "GET / HTTP/1.1\r\n:bad\r\n\r\n";
        BufferedReader br = new BufferedReader(new StringReader(req));
        assertThrows(IOException.class, () -> RequestParser.parse(br));
    }
}