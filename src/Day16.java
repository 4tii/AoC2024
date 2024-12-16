import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day16 {
    private static final int[][] DIRECTIONS = {
            {-1, 0},  // Up
            {0, 1},   // Right
            {1, 0},   // Down
            {0, -1},  // Left
    };

    public static void main(String[] args) throws IOException {
        char[][] map;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day16.txt"))) {
            map = lines.map(String::toCharArray).toArray(char[][]::new);
        }

        List<CostState> tilesOnShortestPath = findShortestPaths(map);
        System.out.println(tilesOnShortestPath.getFirst().cost);
        //Part2
        Set<Coordinate> uniqueTiles = new HashSet<>();
        tilesOnShortestPath.forEach(state -> uniqueTiles.addAll(state.path));
        System.out.println(uniqueTiles.size());
    }

    public static List<CostState> findShortestPaths(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        int[] start = new int[] {map.length-2, 1};
        int[] end = new int[] {1, map[0].length-2};

        List<CostState> pathList = new ArrayList<>();
        PriorityQueue<CostState> pq = new PriorityQueue<>();
        int[][][] dist = new int[rows][cols][4];
        for (int[][] layer : dist) {
            for (int[] row : layer) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }

        pq.offer(new CostState(start[0], start[1], 1, 0, new HashSet<>()));
        dist[start[0]][start[1]][1] = 0;

        while (!pq.isEmpty()) {
            CostState cur = pq.poll();

            if (cur.cost > dist[cur.x][cur.y][cur.dir]) continue;

            if (cur.x == end[0] && cur.y == end[1]) {
                pathList.add(cur);
                continue;
            }

            for(int i = -1; i<2; i++){
                int newDir = (cur.dir+i+4)%4;
                int rotationCost = i != 0 ? 1000 : 0;

                int newX = cur.x + DIRECTIONS[newDir][0];
                int newY = cur.y + DIRECTIONS[newDir][1];

                if (map[newX][newY] != '#') {
                    int newCost = cur.cost + 1 + rotationCost;

                    if (newCost < dist[newX][newY][newDir]) {
                        dist[newX][newY][newDir] = newCost;
                        pq.offer(new CostState(newX, newY, newDir, newCost, cur.path));
                    } else if(newCost == dist[newX][newY][newDir]) {
                        pq.offer(new CostState(newX, newY, newDir, newCost, cur.path));
                    }
                }
            }
        }
        return pathList;
    }
}

class CostState implements Comparable<CostState> {
    int x;
    int y;
    int dir;
    int cost;
    Set<Coordinate> path;

    public CostState(int x, int y, int dir, int cost, Set<Coordinate> path) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.cost = cost;
        this.path = new HashSet<>(path);
        this.path.add(new Coordinate(x, y));
    }

    @Override
    public int compareTo(CostState other) {
        return Integer.compare(this.cost, other.cost);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CostState costState = (CostState) o;
        return x == costState.x && y == costState.y && dir == costState.dir && cost == costState.cost && this.path.equals(costState.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dir, cost, path);
    }
}
