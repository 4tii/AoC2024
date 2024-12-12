import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Day12 {
    private static final int[][] DIRECTIONS = {
            {-1, 0},  // Up
            {0, 1},   // Right
            {1, 0},   // Down
            {0, -1},  // Left
    };

    public static void main(String[] args) throws IOException {
        char[][] map;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day12.txt"))) {
            map = lines.map(String::toCharArray).toArray(char[][]::new);
        }

        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];

        int sum1 = 0;
        int sum2 = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    int[] size = new int[1];
                    int[] perimeter = new int[1];
                    Set<String> sides = new HashSet<>();
                    char regionChar = map[i][j];
                    checkRegions(map, visited, i, j, size, perimeter, sides, regionChar);
                    sum1 += size[0]*perimeter[0];
                    sum2 += size[0]*sides.size();
                }
            }
        }
        System.out.println(sum1);

        //Part2
        System.out.println(sum2);
    }

    private static void checkRegions(char[][] map, boolean[][] visited, int row, int col, int[] size, int[] perimeter, Set<String> sides, char regionChar) {
        visited[row][col] = true;
        size[0]++;

        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow < 0 || newCol < 0 || newRow >= map.length || newCol >= map[0].length || map[newRow][newCol] != regionChar) {
                perimeter[0]++;
                checkSide(newRow, newCol, dir, sides, map, regionChar);
            } else if (!visited[newRow][newCol]) {
                checkRegions(map, visited, newRow, newCol, size, perimeter, sides, regionChar);
            }
        }
    }

    private static void checkSide(int row, int col, int[] direction, Set<String> sides, char[][] map, char regionChar) {
        int checkRow = row;
        int checkCol = col;
        int checkRow2 = row;
        int checkCol2 = col;
        boolean outside = false;
        boolean outside2 = false;
        do {
            if(direction == DIRECTIONS[0]){
                checkRow = row + 1;
                checkCol--;
                checkRow2 = row;
                checkCol2--;
            } else if(direction == DIRECTIONS[1]) {
                checkRow--;
                checkCol = col - 1;
                checkRow2--;
                checkCol2 = col;
            } else if(direction == DIRECTIONS[2]) {
                checkRow = row - 1;
                checkCol--;
                checkRow2 = row;
                checkCol2--;
            } else if(direction == DIRECTIONS[3]) {
                checkRow--;
                checkCol = col +1;
                checkRow2--;
                checkCol2 = col;
            }
            if(checkRow < 0 || checkCol < 0 || checkRow >= map.length || checkCol >= map[0].length)
                outside = true;
            if(checkRow2 < 0 || checkCol2 < 0 || checkRow2 >= map.length || checkCol2 >= map[0].length)
                outside2 = true;
        } while ((!outside && map[checkRow][checkCol] == regionChar)
             && (outside2 || map[checkRow2][checkCol2] != regionChar));

        String sideStartingPoint = "";
        if(direction == DIRECTIONS[0]){
            sideStartingPoint = "H:" + (checkRow-1) + ":" + (checkCol+1);
        } else if(direction == DIRECTIONS[1]) {
            sideStartingPoint = "V:" + (checkRow+1) + ":" + (checkCol+1);
        } else if(direction == DIRECTIONS[2]) {
            sideStartingPoint = "H:" + (checkRow) + ":" + (checkCol+1);
        } else if(direction == DIRECTIONS[3]) {
            sideStartingPoint = "V:" + (checkRow+1) + ":" + (checkCol);
        }
        sides.add(sideStartingPoint);
    }
}
