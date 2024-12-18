import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day18 {
    private static final int[][] DIRECTIONS = {
            {-1, 0},  // Up
            {0, 1},   // Right
            {1, 0},   // Down
            {0, -1},  // Left
    };

    public static void main(String[] args) throws IOException {
        List<Coordinate> bytes;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day18.txt"))) {
            bytes = lines.map(line -> line.split(","))
                    .map(parts -> new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])))
                    .toList();
        }
        int mapSize = 71;
        int simulationSize = 1024;
        char[][] map = new char[mapSize][mapSize];

        for(int i=0; i<simulationSize; i++) {
            Coordinate coord = bytes.get(i);
            map[coord.row][coord.col] = '#';
        }
        CostState curState = findShortestPath(map);
        assert curState != null;
        System.out.println(curState.cost);

        //Part 2
        for(int i=simulationSize; i<bytes.size(); i++) {
            Coordinate coord = bytes.get(i);
            map[coord.row][coord.col] = '#';

            if(curState.path.contains(new Coordinate(coord.row, coord.col))) {
                curState = findShortestPath(map);
                if (curState == null) {
                    System.out.println(bytes.get(i));
                    break;
                }
            }
        }
    }

    public static CostState findShortestPath(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        int[] start = new int[] {0,0};
        int[] end = new int[] {map.length-1,map[0].length-1};

        PriorityQueue<CostState> pq = new PriorityQueue<>();
        int[][][] dist = new int[rows][cols][4];
        for (int[][] layer : dist) {
            for (int[] row : layer) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }

        pq.offer(new CostState(start[0], start[1], 0, 0, new HashSet<>()));
        dist[start[0]][start[1]][0] = 0;

        while (!pq.isEmpty()) {
            CostState cur = pq.poll();

            if (cur.cost > dist[cur.x][cur.y][cur.dir]) continue;

            if (cur.x == end[0] && cur.y == end[1]) {
                return cur;
            }

            for(int i = 0; i<4; i++){
                int newX = cur.x + DIRECTIONS[i][0];
                int newY = cur.y + DIRECTIONS[i][1];

                if(newX >= 0 && newX < rows && newY >= 0 && newY < cols && map[newX][newY] != '#') {
                    int newCost = cur.cost + 1;

                    if (newCost < dist[newX][newY][i]) {
                        dist[newX][newY][i] = newCost;
                        pq.offer(new CostState(newX, newY, i, newCost, cur.path));
                    }
                }
            }
        }
        return null;
    }
}
