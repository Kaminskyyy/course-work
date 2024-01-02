package server.threadpool;

import index.InvertedIndex;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadPool {
    private final InvertedIndex index;
    private final ConnectionQueue queue;
    private final AtomicBoolean isTerminated;
    private boolean isInitialized;
    private final int workersNum;
    private final List<Thread> workers;
    private final Object waiter;
    private final ReadWriteLock lock;

    public ThreadPool(InvertedIndex index, int queueSize, int workersNum) {
        if (queueSize < 1) queueSize = 10;
        if (workersNum < 1) workersNum = 6;

        this.index = index;
        this.queue = new ConnectionQueue(queueSize);
        this.isTerminated = new AtomicBoolean(false);
        this.isInitialized = false;
        this.workersNum = workersNum;
        this.workers = new LinkedList<>();
        this.waiter = new Object();
        this.lock = new ReentrantReadWriteLock();
    }

    public void initialize() {
        lock.writeLock().lock();
        try {
            initializeUnsafe();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void initializeUnsafe() {
        if (isInitialized && !isTerminated.get()) return;
        for (int i = 0; i < workersNum; i++) {
            workers.add(new Worker(index, queue, isTerminated, waiter));
        }

        for (var worker: workers) {
            worker.start();
        }

        isInitialized = true;
    }

    public void terminate() {
        lock.writeLock().lock();
        try {
            if (!isWorkingUnsafe()) return;
            isTerminated.set(true);
        } finally {
            lock.writeLock().unlock();
        }

        synchronized (waiter) {
            waiter.notifyAll();
        }

        for (var worker: workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isInitialized = false;
        isTerminated.set(false);
    }

    public boolean addConnection(Socket connection) {
        lock.writeLock().lock();
        try {
            return addConnectionUnsafe(connection);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean addConnectionUnsafe(Socket connection) {
        if (!isWorkingUnsafe()) return false;

        var isAdded = queue.add(connection);
        if (isAdded) {
            synchronized (waiter) {
                waiter.notify();
            }
        }
        return isAdded;
    }

    public boolean isWorking() {
        lock.readLock().lock();
        try {
            return isWorkingUnsafe();
        } finally {
            lock.readLock().unlock();
        }
    }

    private boolean isWorkingUnsafe() {
        return isInitialized && !isTerminated.get();
    }
}
