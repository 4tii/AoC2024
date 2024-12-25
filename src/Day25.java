import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day25 {
    public static void main(String[] args) throws IOException {
        List<String> input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day25.txt"))) {
            input = lines.toList();
        }

        List<char[][]> schematics = Arrays.stream(String.join("\n", input)
                        .split("\n\\s*\n"))
                        .map(block -> block.split("\n"))
                        .map(l -> Arrays.stream(l).map(String::toCharArray).toArray(char[][]::new))
                        .toList();

        List<int[]> keys = new ArrayList<>();
        List<int[]> locks = new ArrayList<>();

        for(char[][] schematic : schematics){
            if(schematic[0][0]=='#')
                locks.add(countHashes(schematic));
            else
                keys.add(countHashes(schematic));
        }

        int sum = (int) keys.stream()
                    .flatMapToLong(key -> locks.stream()
                        .mapToLong(lock -> IntStream.range(0, lock.length)
                            .allMatch(i -> key[i] + lock[i] <= 5) ? 1L : 0L))
                    .sum();

        System.out.println(sum);
    }

    public static int[] countHashes(char[][] array) {
        int rows = array.length;
        int cols = array[0].length;
        int[] counts = new int[cols];

        for (int col = 0; col < cols; col++) {
            for (int row = 1; row < rows-1; row++) {
                if (array[row][col] == '#')
                    counts[col]++;
            }
        }
        return counts;
    }
}
