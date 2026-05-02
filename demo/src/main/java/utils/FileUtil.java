package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//handle file I/O

public final class FileUtil {

    private FileUtil() {
    }

    public static boolean fileExists(String path) {
        return Files.exists(Paths.get(path));
    }

    public static List<String> readLines(String path) {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(Files.readAllLines(p, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("FileUtil: failed to read " + path + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static void writeLines(String path, List<String> lines) throws IOException {
        Path p = Paths.get(path);
        if (p.getParent() != null) {
            Files.createDirectories(p.getParent());
        }
        Files.write(
                p,
                lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    public static void appendLine(String path, String line) throws IOException {
        Path p = Paths.get(path);
        if (p.getParent() != null) {
            Files.createDirectories(p.getParent());
        }
        Files.write(
                p,
                List.of(line),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND,
                StandardOpenOption.WRITE);
    }
}
