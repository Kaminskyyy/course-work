package server.threadpool;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectionQueue {
    private final Queue<Socket> queue;
    private final ReentrantReadWriteLock lock;
    private final int maxConnections;

    public ConnectionQueue(int queueSize) {
        maxConnections = queueSize;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public boolean add(Socket connection) {
        lock.writeLock().lock();
        try {
            if (queue.size() == maxConnections) return false;
            queue.add(connection);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Socket pop() {
        lock.writeLock().lock();
        try {
            return queue.poll();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean empty() {
        lock.readLock().lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return queue.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
