package index;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InvertedIndexCreator {
    private final List<Path> files;
    private final InvertedIndex index;
    private final int threadsNum;

    public InvertedIndexCreator(List<Path> files, int threadsNum, InvertedIndex index) {
        this.files = files;
        this.index = index;
        this.threadsNum = threadsNum;
    }

    public void create() throws InterruptedException {
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

        System.out.println("Time taken: " + (finishTime - startTime));
    }
}
