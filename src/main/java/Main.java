import server.HttpServer;
import server.Router;
import server.RouterConfig;

public class Main {
    public static void main(String[] args) {
        String dirName = args.length > 1 ? args[1] : ".";
        Router router = new RouterConfig(dirName).getRouter();
        new HttpServer(4221, router).start();
    }
}
