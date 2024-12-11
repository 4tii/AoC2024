import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day11 {

    private static final Map<String, Long> cache = new HashMap<>();
    public static void main(String[] args) throws IOException {
        List<Long> input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day11.txt"))) {
            input = lines
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .map(Long::parseLong)
                    .toList();
        }

        long sum = 0;
        for(Long l: input) {
            sum += checkNumber(l, 0, 25);
        }
        System.out.println(sum);

        //Part 2
        cache.clear();
        sum = 0;
        for(Long l: input) {
            sum += checkNumber(l, 0, 75);
        }
        System.out.println(sum);

    }

    public static long checkNumber(Long number, int depth, int maxDepth){
        if(depth==maxDepth)
            return 1;

        if(cache.containsKey(number + "-" + depth))
            return cache.get(number + "-" + depth);

        if(number==0) {
            long sum = checkNumber(1L, depth + 1, maxDepth);
            cache.put(1L+ "-" + (depth+1), sum);
            return sum;
        }else if(isEvenDigitCount(number)){
            String numberStr = String.valueOf(number);
            int mid = numberStr.length() / 2;

            long firstPart = Long.parseLong(numberStr.substring(0, mid));
            long secondPart = Long.parseLong(numberStr.substring(mid));

            long sum = checkNumber(firstPart, depth+1, maxDepth);
            long sum2 = checkNumber(secondPart, depth+1, maxDepth);
            cache.put(firstPart+ "-" + (depth+1), sum);
            cache.put(secondPart+ "-" + (depth+1), sum2);
            return sum + sum2;
        } else {
            long sum = checkNumber(number * 2024L, depth + 1, maxDepth);
            cache.put((number*2024L) + "-" + (depth+1), sum);
            return sum;
        }
    }

    private static boolean isEvenDigitCount(long number) {
        int digitCount = (int) Math.log10(number) + 1;
        return digitCount % 2 == 0;
    }

}
