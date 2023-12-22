import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FileScanner {
    private final static Queue<Path> directoriesToScan = new LinkedList<>();
    private final static List<Path> files = new ArrayList<>();

    public static List<Path> scan(String rootDirectory) throws IOException {
        var rootDirectoryPath = Path.of(rootDirectory);
        directoriesToScan.add(rootDirectoryPath);

        while (!directoriesToScan.isEmpty()) {
            scan0(directoriesToScan.remove());
        }

        return files;
    }

    private static void scan0(Path path) throws IOException {
        try(var entry = Files.list(path)) {
            entry.forEach((item) -> {
                if (Files.isDirectory(item)) {
                    directoriesToScan.add(item);
                } else {
                    files.add(item);
                }
            });
        }
    }
}
