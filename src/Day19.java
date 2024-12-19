import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> towels;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day19_1.txt"))) {
            towels = lines.flatMap(line -> Arrays.stream(line.split(",")))
                    .map(String::trim)
                    .toList();
        }
        List<String> designs;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day19_2.txt"))) {
            designs = lines.toList();
        }

        Map<String, Long> cache = new HashMap<>();
        int sum = 0;
        long totalCombinations = 0;
        for (String design : designs) {
            long combinations = checkCombinations(towels, design, cache);
            if(combinations!=0) {
                sum++;
                totalCombinations += combinations;
            }
        }
        System.out.println(sum);

        //Part 2
        System.out.println(totalCombinations);
    }

    private static long checkCombinations(List<String> towels, String design, Map<String, Long> cache){
        if(design.isEmpty())
            return 1;

        if(cache.containsKey(design))
            return cache.get(design);

        long count = 0;

        for(String towel : towels){
            if(design.startsWith(towel)){
                String remaining = design.substring(towel.length());
                count += checkCombinations(towels, remaining, cache);
            }
        }
        cache.put(design, count);
        return count;
    }
}
