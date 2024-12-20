import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day20 {
    private static final int[][] DIRECTIONS = {
            {-1, 0},  // Up
            {0, 1},   // Right
            {1, 0},   // Down
            {0, -1},  // Left
    };

    private static final int MIN_DISTANCE_SAVED = 100;

    public static void main(String[] args) throws IOException {
        char[][] map;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day20.txt"))) {
            map = lines.map(String::toCharArray).toArray(char[][]::new);
        }

        Coordinate start = findPosition(map, 'S');
        Coordinate end = findPosition(map, 'E');

        List<Coordinate> path = findPath(map, start, end);

        findShortcuts(path, 2);

        //Part 2
        findShortcuts(path, 20);
    }

    private static Coordinate findPosition(char[][] map, char target) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == target)
                    return new Coordinate(i, j);
            }
        }
        return null;
    }

    private static List<Coordinate> findPath(char[][] map, Coordinate start, Coordinate end) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Coordinate> queue = new LinkedList<>();
        List<Coordinate> path = new ArrayList<>();
        path.add(start);

        queue.add(start);
        visited[start.row][start.col] = true;

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            if (current.equals(end)) {
                return path;
            }

            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && !visited[newRow][newCol] && map[newRow][newCol] != '#') {
                    queue.add(new Coordinate(newRow, newCol));
                    visited[newRow][newCol] = true;
                    path.add(new Coordinate(newRow, newCol));
                }
            }
        }
        return Collections.emptyList();
    }

    private static void findShortcuts(List<Coordinate> mainPath, int maxShortcutLength) {
        int cnt=0;
        for(int i=0; i<mainPath.size(); i++) {
            Coordinate current = mainPath.get(i);
            for(int j=i+MIN_DISTANCE_SAVED; j<mainPath.size(); j++) {
                Coordinate next = mainPath.get(j);
                int dist = Math.abs(current.row - next.row) + Math.abs(current.col - next.col);
                if(dist <= maxShortcutLength && j - i - dist >= MIN_DISTANCE_SAVED) {
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }
}
