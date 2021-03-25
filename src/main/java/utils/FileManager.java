package main.java.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {
    public static List<String> loadFile(String path) throws IOException {
        return Files.readAllLines(new File(path).toPath(), Charset.defaultCharset());
    }

    public static void writeFile(String path, List<String> fileLines) throws IOException {
        Path output = Paths.get(path);
        Files.write(output, fileLines, Charset.defaultCharset());
    }
}
