package InvertedIndex;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InvertedIndex {
    private final Map<String, InvertedIndexEntry> index = new HashMap<String, InvertedIndexEntry>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void add(String word, Path path, int wordNumber) {
        lock.writeLock().lock();
        try {
            var entry = index.get(word);
            if (entry == null) {
                var newEntry = new InvertedIndexEntry();
                newEntry.addLocation(path, wordNumber);
                index.put(word, newEntry);
            } else {
                entry.addLocation(path, wordNumber);
            }
        } finally {
            lock.writeLock().unlock();
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

    public Map<Path, List<Integer>> get(String word) {
        lock.readLock().lock();
        try {
            return index.get(word).getLocations();
        } finally {
            lock.writeLock().lock();
        }
    }
}
