package server;

import model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Router {
    private final HashMap<String, Handler> routes = new HashMap<>();
    private final List<Function<Handler, Handler>> middlewares = new ArrayList<>();

    private Handler defaultHandler = req -> Response.notFound("");

    public void use(Function<Handler, Handler> middleware) {
        middlewares.add(middleware);
    }

    public void add(String method, String path, Handler handler) {
        Handler wrapped = handler;
        for (Function<Handler, Handler> mw : middlewares) {
            wrapped = mw.apply(wrapped);
        }
        routes.put(method + " " + path, wrapped);
    }

    public Handler find(String method, String path) {
        String exactKey = method + " " + path;
        if (routes.containsKey(exactKey)) {
            return routes.get(exactKey);
        }
        for (String routeKey : routes.keySet()) {
            if (routeKey.endsWith("/*")) {
                String prefix = routeKey.substring(0, routeKey.length() - 1); // remove the *
                if (exactKey.startsWith(prefix)) {
                    return routes.get(routeKey);
                }
            }
        }
        return defaultHandler;
    }
}
