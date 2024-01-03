package index;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InvertedIndex {
    void add(String word, Path path, int wordNumber);

    Map<Path, List<Integer>> get(String word);

    int size();

    int deepSize();
}
