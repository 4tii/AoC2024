import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<Long> results = new ArrayList<>();
        List<long[]> numbers = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day7.txt"))) {
            lines.map(line -> line.split(":"))
                    .forEach(parts -> {
                        results.add(Long.parseLong(parts[0].trim()));
                        numbers.add(Stream.of(parts[1].trim().split("\\s+"))
                                .mapToLong(Long::parseLong)
                                .toArray());
                    });
        }
        Long sum = IntStream.range(0, results.size())
                .filter(i -> canCalculate(numbers.get(i), results.get(i), numbers.get(i)[0], 1, false))
                .mapToLong(results::get)
                .sum();
        System.out.println(sum);

        //Part 2
        sum = IntStream.range(0, results.size())
                .filter(i -> canCalculate(numbers.get(i), results.get(i), numbers.get(i)[0], 1, true))
                .mapToLong(results::get)
                .sum();
        System.out.println(sum);
    }

    public static boolean canCalculate(long[] numbers, Long target, Long current, int index, boolean withConcat){
        if(index == numbers.length)
            return Objects.equals(current, target);

        return canCalculate(numbers, target, current+numbers[index], index+1, withConcat)
                || canCalculate(numbers, target, current*numbers[index], index+1, withConcat)
                || (withConcat && canCalculate(numbers, target, Long.parseLong(current + "" + numbers[index]), index+1, true));
    }
}
