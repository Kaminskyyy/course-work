package index;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class INCreatorWorker extends Thread {
    private final int start;
    private final int end;
    private final List<Path> files;
    private final InvertedIndex index;

    public INCreatorWorker(int start, int end, List<Path> files, InvertedIndex index) {
        this.start = start;
        this.end = end;
        this.files = files;
        this.index = index;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            Path path = files.get(i);

            String fileContent;
            try {
                fileContent = Files.readString(path);
            } catch (IOException e) {
                continue;
            }

            var words = fileContent.toLowerCase().split("\\W+");

            for (var j = 0; j < words.length; j++) {
                index.add(words[j], path, j);
            }
        }
    }
}
