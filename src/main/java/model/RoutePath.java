package model;

public enum RoutePath {
    ROOT("/"),
    USER_AGENT("/user-agent"),
    ECHO("/echo"),
    FILES("/files");

    public final String path;

    RoutePath(String path) {
        this.path = path;
    }
}
