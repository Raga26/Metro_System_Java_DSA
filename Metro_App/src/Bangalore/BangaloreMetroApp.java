package Bangalore;

import java.util.*;

class MetroStation {
    String name;

    MetroStation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class MetroGraph {
    private Map<MetroStation, List<MetroStation>> adjacencyList;

    MetroGraph() {
        this.adjacencyList = new HashMap<>();
    }

    void addStation(MetroStation station, List<MetroStation> connections) {
        adjacencyList.put(station, connections);
    }

    void deleteStation(MetroStation station) {
        adjacencyList.remove(station);
        // Remove the station from connections of other stations
        for (List<MetroStation> connections : adjacencyList.values()) {
            connections.remove(station);
        }
    }

    List<MetroStation> getConnections(MetroStation station) {
        return adjacencyList.get(station);
    }

    Map<MetroStation, List<MetroStation>> getAdjacencyList() {
        return adjacencyList;
    }

    // Additional method to check if a station exists in the graph
    boolean stationExists(MetroStation station) {
        return adjacencyList.containsKey(station);
    }
}

public class BangaloreMetroApp {

    public static void main(String[] args) {
        MetroGraph metroGraph = createSampleMetroGraph();

        // Print the adjacency list
        System.out.println("Metro Network Adjacency List:");
        metroGraph.getAdjacencyList().forEach((station, connections) ->
                System.out.println(station + " -> " + connections));

        MetroStation source = new MetroStation("MG Road");
        MetroStation destination = new MetroStation("Banashankari");

        // Find the shortest path using BFS
        List<MetroStation> shortestPathBFS = findShortestPathBFS(metroGraph, source, destination);
        System.out.println("\nShortest Path from " + source + " to " + destination + " (BFS): " + shortestPathBFS);

        // Find the longest path using DFS
        List<MetroStation> longestPathDFS = findLongestPathDFS(metroGraph, source);
        System.out.println("Longest Path from " + source + " (DFS): " + longestPathDFS);

        // Add a new station
        MetroStation newYeshwanthpur = new MetroStation("Yeshwanthpur");
        metroGraph.addStation(newYeshwanthpur, Arrays.asList(source, destination));

        // Print the updated adjacency list
        System.out.println("\nUpdated Metro Network Adjacency List after adding Yeshwanthpur:");
        metroGraph.getAdjacencyList().forEach((station, connections) ->
                System.out.println(station + " -> " + connections));

        // Delete a station
        MetroStation stationToDelete = new MetroStation("MG Road");
        if (metroGraph.stationExists(stationToDelete)) {
            metroGraph.deleteStation(stationToDelete);
            System.out.println("\nUpdated Metro Network Adjacency List after deleting " + stationToDelete + ":");
            metroGraph.getAdjacencyList().forEach((station, connections) ->
                    System.out.println(station + " -> " + connections));
        } else {
            System.out.println("Station " + stationToDelete + " does not exist in the network.");
        }
    }

    private static MetroGraph createSampleMetroGraph() {
        MetroGraph metroGraph = new MetroGraph();

        MetroStation mgRoad = new MetroStation("MG Road");
        MetroStation indiranagar = new MetroStation("Indiranagar");
        MetroStation jayanagar = new MetroStation("Jayanagar");
        MetroStation majestic = new MetroStation("Majestic");
        MetroStation banashankari = new MetroStation("Banashankari");

        metroGraph.addStation(mgRoad, Arrays.asList(indiranagar, majestic));
        metroGraph.addStation(indiranagar, Arrays.asList(mgRoad, jayanagar));
        metroGraph.addStation(jayanagar, Arrays.asList(indiranagar, majestic, banashankari));
        metroGraph.addStation(majestic, Arrays.asList(mgRoad, indiranagar, jayanagar));
        metroGraph.addStation(banashankari, Arrays.asList(jayanagar));

        return metroGraph;
    }

    private static List<MetroStation> findShortestPathBFS(MetroGraph graph, MetroStation source, MetroStation destination) {
        Queue<MetroStation> queue = new LinkedList<>();
        Map<MetroStation, MetroStation> parentMap = new HashMap<>();
        Set<MetroStation> visited = new HashSet<>();

        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            MetroStation current = queue.poll();
            List<MetroStation> connections = graph.getConnections(current);

            if (connections != null) {
                for (MetroStation neighbor : connections) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        parentMap.put(neighbor, current);
                    }

                    if (neighbor.equals(destination)) {
                        // Reconstruct the path from destination to source
                        List<MetroStation> path = new ArrayList<>();
                        while (neighbor != null) {
                            path.add(neighbor);
                            neighbor = parentMap.get(neighbor);
                        }
                        Collections.reverse(path);
                        return path;
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private static List<MetroStation> findLongestPathDFS(MetroGraph graph, MetroStation source) {
        Map<MetroStation, Integer> distances = new HashMap<>();
        List<MetroStation> longestPath = new ArrayList<>();

        dfs(graph, source, distances, longestPath);

        return longestPath;
    }

    private static int dfs(MetroGraph graph, MetroStation current, Map<MetroStation, Integer> distances, List<MetroStation> longestPath) {
        List<MetroStation> connections = graph.getConnections(current);

        if (connections != null) {
            int maxDistance = 0;
            for (MetroStation neighbor : connections) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, 1 + dfs(graph, neighbor, distances, longestPath));
                }

                if (distances.get(neighbor) > maxDistance) {
                    maxDistance = distances.get(neighbor);
                    longestPath.clear();
                    longestPath.add(current);
                    longestPath.addAll(graph.getConnections(current));
                }
            }
            return maxDistance;
        }

        return 0;
    }
}
