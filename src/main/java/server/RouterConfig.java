package server;
import middleware.Gzip;
import model.Response;
import model.RoutePath;

import java.util.Map;

import static utils.FilesUtils.readFromFile;
import static utils.FilesUtils.writeToFile;

public class RouterConfig {
    private final Router router;

    public RouterConfig(String dirname) {
        this.router = new Router();
        router.use(Gzip.withGzip());
        this.registerBaseRoutes();
        this.registerFileRoutes(dirname);
    }

    public void registerBaseRoutes() {
        router.add("GET", RoutePath.ROOT.path, req -> Response.ok("Welcome"));

        router.add("GET", RoutePath.USER_AGENT.path, req ->
                Response.ok(req.headers().getOrDefault("user-agent", "Unknown"))
        );

        router.add("GET", RoutePath.ECHO.path + "/*", req -> {
                    String requestedString = req.path().substring(RoutePath.ECHO.path.length() + 1);
                    return Response.ok(requestedString);
                }
        );
    }

    public void registerFileRoutes(String dirname) {
        router.add("POST", RoutePath.FILES.path + "/*", req -> {
            String requestedPath = req.path().substring(RoutePath.FILES.path.length() + 1);
            String fullPath = dirname + "/" + requestedPath;
            writeToFile(fullPath, req.body());
            return Response.created("");
        });

        router.add("GET", RoutePath.FILES.path + "/*", req -> {
            String requestedPath = req.path().substring(RoutePath.FILES.path.length() + 1);
            String fullPath = dirname + "/" + requestedPath;
            try {
                String content = readFromFile(fullPath);
                return Response.customMethod(200, "OK", content, Map.of("Content-Type", "application/octet-stream"));
            }
            catch (Exception e) {
                return Response.notFound("");
            }
        });
    }

    public Router getRouter() {
        return router;
    }
}
