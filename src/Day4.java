import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day4 {
    private static final int[][] DIRECTIONS = {
            {0, 1},   // Right
            {0, -1},  // Left
            {1, 0},   // Down
            {-1, 0},  // Up
            {1, 1},   // Down-Right
            {-1, -1}, // Up-Left
            {1, -1},  // Down-Left
            {-1, 1}   // Up-Right
    };

    public static void main(String[] args) throws IOException {
        char[][] input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day4.txt"))) {
            input = lines.map(String::toCharArray).toArray(char[][]::new);
            System.out.println(searchWord(input));
            //Part 2
            System.out.println(searchX(input));
        }
    }

    private static int searchWord(char[][] input){
        int sum=0;
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input[0].length; col++) {
                // Try all directions
                for (int[] direction : DIRECTIONS) {
                    if (checkDirection(input, row, col, direction)) {
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    private static boolean checkDirection(char[][] input, int row, int col, int[] direction) {
        // Check each character in the word
        for (int i = 0; i < 4; i++) {
            // Check boundaries
            if (row < 0 || row >= input.length || col < 0 || col >= input[0].length) {
                return false;
            }
            // Check character match
            if (input[row][col] != "XMAS".charAt(i)) {
                return false;
            }
            // Move in the direction
            row += direction[0];
            col += direction[1];
        }
        return true;
    }

    private static int searchX(char[][] input) {
        int sum=0;
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input[0].length; col++) {
                if (input[row][col] == 'A' && row-1>=0 && row+1<input.length && col-1>=0 && col+1<input[0].length && (
                        ((input[row - 1][col - 1] == 'M' && input[row + 1][col + 1] == 'S') && ((input[row - 1][col + 1] == 'M' && input[row + 1][col - 1] == 'S') || input[row + 1][col - 1] == 'M' && input[row - 1][col + 1] == 'S')) || // Top-left to bottom-right
                        ((input[row - 1][col + 1] == 'M' && input[row + 1][col - 1] == 'S') && ((input[row - 1][col - 1] == 'M' && input[row + 1][col + 1] == 'S') || (input[row + 1][col - 1] == 'M' && input[row - 1][col + 1] == 'S'))) || // Top-right to bottom left
                        ((input[row + 1][col + 1] == 'M' && input[row - 1][col - 1] == 'S') && ((input[row + 1][col - 1] == 'M' && input[row - 1][col + 1] == 'S') || (input[row - 1][col + 1] == 'M' && input[row + 1][col - 1] == 'S'))) || // Bottom-right to top-left
                        ((input[row + 1][col - 1] == 'M' && input[row - 1][col + 1] == 'S') && ((input[row - 1][col - 1] == 'M' && input[row + 1][col + 1] == 'S') || (input[row + 1][col + 1] == 'M' && input[row - 1][col - 1] == 'S'))))) { // Bottom-left to top-right
                    sum++;
                }
            }
        }
        return sum;
    }
}
