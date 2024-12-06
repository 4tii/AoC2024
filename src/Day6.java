import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class Day6 {
    private static final int[][] DIRECTIONS = {
            {-1, 0},  // Up
            {0, 1},   // Right
            {1, 0},   // Down
            {0, -1},  // Left
    };

    public static void main(String[] args) throws IOException {
        char[][] input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day6.txt"))) {
            input = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        int[] startPosition = new int[0];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (input[i][j] == '^') {
                    startPosition = new int[]{i, j};
                }
            }
        }

        int[] position = startPosition;
        int currentDir = 0;
        while(true) {
            int[] newPosition = new int[] {position[0] + DIRECTIONS[currentDir][0], position[1] + DIRECTIONS[currentDir][1]};

            if (newPosition[0] >= input.length || newPosition[0] < 0
                    || newPosition[1] >= input[0].length || newPosition[1] < 0) {
                input[position[0]][position[1]] = 'X';
                break;
            }

            if(input[newPosition[0]][newPosition[1]] == '#') {
                currentDir = (currentDir + 1) % 4;
            }else{
                input[position[0]][position[1]] = 'X';
                position = newPosition;
            }
        }

        System.out.println(Arrays.stream(input)
                .flatMapToInt(row -> new String(row).chars())
                .filter(c -> c == 'X')
                .count());

        //Part 2
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day6.txt"))) {
            input = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        int sum = 0;
        for(int i=0; i < input.length; i++){
            for(int j=0; j < input[i].length; j++){
                if(input[i][j] == '.'){
                    input[i][j] = '#';

                    if(detectLoopFromPosition(input, startPosition))
                        sum++;

                    input[i][j] = '.';
                }
            }
        }
        System.out.println(sum);
    }

    public static boolean detectLoopFromPosition(char[][] input, int[] position){
        int currentDir=0;
        Set<State> visitedStates = new HashSet<>();
        while (true) {
            State currentState = new State(position[0], position[1], currentDir);
            if(visitedStates.contains(currentState)){
                return true;
            }
            visitedStates.add(currentState);

            int[] newPosition = new int[] {position[0] + DIRECTIONS[currentDir][0], position[1] + DIRECTIONS[currentDir][1]};

            if (newPosition[0] >= input.length || newPosition[0] < 0
                    || newPosition[1] >= input[0].length || newPosition[1] < 0)
                return false;

            if(input[newPosition[0]][newPosition[1]] == '#') {
                currentDir = (currentDir + 1) % 4;
            }else{
                position = newPosition;
            }
        }
    }
}

class State {
    int row;
    int col;
    int direction;

    State(int row, int col, int direction) {
        this.row = row;
        this.col = col;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return row == state.row && col == state.col && direction == state.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, direction);
    }

    @Override
    public String toString() {
        return row + "," + col + "," + direction;
    }
}
