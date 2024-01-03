package server.threadpool;

import index.InvertedIndex;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker extends Thread {
    private final InvertedIndex index;
    private final ConnectionQueue queue;
    private final AtomicBoolean isTerminated;
    private final Object waiter;
    private Socket connection;

    public Worker(InvertedIndex index, ConnectionQueue queue, AtomicBoolean isTerminated, Object waiter) {
        this.index = index;
        this.queue = queue;
        this.isTerminated = isTerminated;
        this.waiter = waiter;
    }

    @Override
    public void run() {
        while (true) {
            Socket connection = null;
            try {
                synchronized (waiter) {
                    do {
                        connection = queue.pop();
                        if (connection != null) break;
                        else {
                            waiter.wait();
                        }
                    } while (!isTerminated.get());
                }

                if (connection == null && isTerminated.get() && queue.empty()) return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (connection != null) processConnection(connection);
        }
    }

    private void processConnection(Socket connection) {
        var connectionProcessor = new ConnectionProcessor(connection, index);
        connectionProcessor.process();
    }
}
