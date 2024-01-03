package index;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InvertedIndexCustom implements InvertedIndex {
    private final Map<String, InvertedIndexEntry> index = new HashMap<String, InvertedIndexEntry>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void add(String word, Path path, int wordNumber) {
        var entry = index.get(word);
        if (entry == null) {
            lock.writeLock().lock();
            try {
                addUnsafe(word, path, wordNumber);
                return;
            } finally {
                lock.writeLock().unlock();
            }
        }

        entry.addLocation(path, wordNumber);
    }

    private void addUnsafe(String word, Path path, int wordNumber) {
        var entry = index.get(word);
        if (entry == null) {
            var newEntry = new InvertedIndexEntry();
            newEntry.addLocation(path, wordNumber);
            index.put(word, newEntry);
        } else {
            entry.addLocation(path, wordNumber);
        }
    }

    public void remove(String word) {
        lock.writeLock().lock();
        try {
            index.remove(word);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Map<Path, List<Integer>> get(String word) {
        lock.readLock().lock();
        try {
            var entry = index.get(word);
            if (entry == null) return null;
            return entry.getLocations();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return index.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int deepSize() {
        lock.readLock().lock();
        try {
            var deepSize = 0;

            for (var entry: index.entrySet()) {
                var indexEntry = entry.getValue();
                var locations = indexEntry.getLocationsUnsafe();

                for (var location: locations.entrySet()) {
                    deepSize += location.getValue().size();
                }
            }

            return deepSize;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void printState() {
        for (var entry : index.entrySet()) {
            System.out.println(entry.getKey());
            entry.getValue().printState();
        }
    }
}
