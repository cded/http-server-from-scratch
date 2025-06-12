package model;

import java.util.Map;

public record Request(String method,
                      String path,
                      String httpVersion,
                      Map<String, String> headers,
                      String body) {

}
