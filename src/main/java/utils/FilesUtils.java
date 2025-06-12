package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesUtils {
    public static void writeToFile(String path, String contents) {
        try {
            Files.write(Path.of(path), contents.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + path, e);
        }
    }

    public static String readFromFile(String path) {
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            int c;
            StringBuilder sb = new StringBuilder();
            while((c = is.read()) != -1) {
                sb.append((char) c);
            }
            String content = sb.toString();
            return content;
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }
}
