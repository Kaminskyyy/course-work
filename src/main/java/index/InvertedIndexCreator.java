package index;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InvertedIndexCreator {
    private final String rootDirectory;
    private List<Path> files;
    private final InvertedIndex index;
    private final int threadsNum;
    private long indexCreatingTime;

    public InvertedIndexCreator(String rootDirectory, int threadsNum) {
        this.rootDirectory = rootDirectory;
        this.index = new InvertedIndexCustom();
        this.threadsNum = threadsNum;
    }

    public InvertedIndex create() throws IOException, InterruptedException {
        create0();
        printInfo();

        return index;
    }

    private void create0() throws InterruptedException, IOException {
        var fileScanner = new FileScanner();
        files = fileScanner.scan(rootDirectory);

        var chunkSize = files.size() / threadsNum;
        var threads = new ArrayList<Thread>();

        for (int i = 0; i < threadsNum; i++) {
            var start = chunkSize * i;
            var end = (i == threadsNum - 1) ? files.size() : chunkSize * (i + 1);

            threads.add(
                    new INCreatorWorker(
                            start,
                            end,
                            files,
                            index
                    )
            );
        }

        long startTime = System.currentTimeMillis();
        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            thread.join();
        }
        long finishTime = System.currentTimeMillis();

        indexCreatingTime = finishTime - startTime;
    }

    private void printInfo() {
        System.out.println("\n----------------Inverted index info----------------");
        System.out.println("Number of threads:            " + threadsNum);
        System.out.println("Root directory:               " + rootDirectory);
        System.out.println("Number of files:              " + files.size());

        System.out.println("Index size:                   " + index.size());
        System.out.println("Index deep size:              " + index.deepSize());
        System.out.println("Time spent on index creation: " + indexCreatingTime + " ms");
        System.out.println("---------------------------------------------------\n");
    }
}
