import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        String rootDirectory = "D:/KPI/4_curs/cursach/data";
        var files = FileScanner.scan(rootDirectory);

        for (var file : files) {
            System.out.println(file);
        }
    }
}
