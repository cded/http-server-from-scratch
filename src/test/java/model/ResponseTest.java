package model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {
    @Test
    public void testWriteToStream() throws Exception {
        Response r = Response.ok("hi");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        r.writeTo(out);
        String resText = out.toString(StandardCharsets.UTF_8);
        assertTrue(resText.startsWith("HTTP/1.1 200 OK"));
        assertTrue(resText.contains("Content-Length: 2"));
        assertTrue(resText.endsWith("hi"));
    }
}