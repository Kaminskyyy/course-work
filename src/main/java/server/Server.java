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

        var port = 8080;
        try {
            var server = new ServerSocket(port);

            printInfo(port);

            while (true) {
                var clientSocket = server.accept();

                var isAdded = threadPool.addConnection(clientSocket);
                if (!isAdded) refuseConnection(clientSocket);
                else System.out.println(Thread.currentThread().getName() + "\tConnection pending: " + clientSocket.getRemoteSocketAddress());
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
            System.out.println(Thread.currentThread().getName() + "\tConnection refused: " + connection.getRemoteSocketAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printInfo(int port) {
        System.out.println("\n----------------Socket server info-----------------");
        System.out.println("Port:                   " + port);
        System.out.println("Connections queue size: " + queueSize);
        System.out.println("Thread pool size:       " + maxConnections);
        System.out.println("---------------------------------------------------\n");
    }
}
