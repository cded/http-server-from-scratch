package server;

import model.Request;
import model.Response;

@FunctionalInterface
public interface Handler {
    Response handle(Request request);
}
