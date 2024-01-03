import index.InvertedIndexCreator;
import server.Server;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var commandLineArguments = new CommandLineArguments();
        commandLineArguments.parse(args);

        var invertedIndexCreator = new InvertedIndexCreator(
                commandLineArguments.getRoot(),
                commandLineArguments.getNumThreads()
        );
        var index = invertedIndexCreator.create();

        var server = new Server(
                index,
                commandLineArguments.getThreadPoolSize(),
                commandLineArguments.getQueueSize()
        );
        server.start();
    }
}