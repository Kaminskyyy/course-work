package index;

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

    synchronized public void addLocation(Path path, int wordNumber) {
        var locationsList = locations.get(path);

        if (locationsList == null) {
            locationsList = new ArrayList<Integer>();
            locationsList.add(wordNumber);
            locations.put(path, locationsList);
        } else {
            locationsList.add(wordNumber);
        }
    }

    synchronized public void printState() {
        for (var path : locations.entrySet()) {
            System.out.println(path.getKey());
            System.out.println(path.getValue());
        }
    }

    synchronized public Map<Path, List<Integer>> getLocations() {
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
