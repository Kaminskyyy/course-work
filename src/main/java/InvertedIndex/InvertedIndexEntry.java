package InvertedIndex;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndexEntry {
    private final Map<Path, List<Integer>> locations;

    public InvertedIndexEntry() {
        this.locations = new HashMap<>();
    }

    public void addLocation(Path path, int wordNumber) {
        var locationsList = locations.get(path);

        if (locationsList == null) {
            locationsList = new ArrayList<Integer>();
            locationsList.add(wordNumber);
            locations.put(path, locationsList);
        } else {
            locationsList.add(wordNumber);
        }
    }

    public void printState() {
        for (var path : locations.entrySet()) {
            System.out.println(path.getKey());
            System.out.println(path.getValue());
        }
    }

    public Map<Path, List<Integer>> getLocations() {
        return locationsDeepCopy();
    }

    public Map<Path, List<Integer>> getLocationsUnsafe() {
        return locations;
    }

    private Map<Path, List<Integer>> locationsDeepCopy() {
        var copy = new HashMap<Path, List<Integer>>();

        for (var entry : locations.entrySet()) {
            var locationsListCopy = new ArrayList<Integer>();

            for (var location : entry.getValue()) {
                locationsListCopy.add(location);
            }

            copy.put(
                    Path.of(entry.getKey().toString()),
                    locationsListCopy
            );
        }

        return copy;
    }
}
