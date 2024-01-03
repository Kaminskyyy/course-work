package server;

import index.InvertedIndex;
import server.threadpool.ThreadPool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final InvertedIndex index;
    private final int maxConnections;
    private final int queueSize;

    public Server(InvertedIndex index, int maxConnections, int queueSize) {
        this.index = index;
        this.maxConnections = maxConnections;
        this.queueSize = queueSize;
    }

    public void start() {
        var threadPool = new ThreadPool(index, queueSize, maxConnections);
        threadPool.initialize();

        try {
            var server = new ServerSocket(8080);
            while (true) {
                var clientSocket = server.accept();
                var isAdded = threadPool.addConnection(clientSocket);
                if (!isAdded) refuseConnection(clientSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refuseConnection(Socket connection) {
        try {
            var out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            out.write("Connection refused");
            out.write("/end");
            out.flush();

            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
