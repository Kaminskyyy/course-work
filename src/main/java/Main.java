import index.InvertedIndex;
import index.InvertedIndexCreator;
import index.InvertedIndexCustom;
import server.Server;
import server.threadpool.ThreadPool;

import java.io.*;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var index = createIndex(args);
        var server = new Server(index, 2, 2);
        server.start();
    }

    private static InvertedIndex createIndex(String[] args) throws IOException, InterruptedException {
        var threadsNum = 6;
        var rootDirectory = "D:/KPI/4_curs/cursach/data";
        if (args.length != 0) {
            threadsNum = Integer.valueOf(args[0]);
            rootDirectory = args[1];
        }

        System.out.println("Threads number: " + threadsNum);
        System.out.println("Root directory: " + rootDirectory);

        var files = FileScanner.scan(rootDirectory);

        System.out.println(files.size());

        var index = new InvertedIndexCustom();
        var invertedIndexCreator = new InvertedIndexCreator(files, threadsNum, index);

        invertedIndexCreator.create();

        System.out.println(index.size());
        System.out.println(index.deepSize());
        System.out.println(index.get("is").size());

        return index;
    }
}