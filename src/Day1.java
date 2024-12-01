import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day1.txt"))) {
            lines
                .map(line -> line.split(" "))
                .forEach(parts -> {
                    l1.add(Integer.parseInt(parts[0]));
                    l2.add(Integer.parseInt(parts[3]));
                });
        }
        l1.sort(Comparator.comparingInt(Math::abs));
        l2.sort(Comparator.comparingInt(Math::abs));

        List<Integer> diff = IntStream.range(0, l1.size())
                .mapToObj(i -> Math.abs(l1.get(i) - l2.get(i)))
                .toList();

        Integer sum = diff.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println(sum);


        //Part 2
        List<Long> simScore = new ArrayList<>();
        l1.forEach(e -> simScore.add(l2.stream().filter(e2 -> Objects.equals(e2, e)).count()*e));

        Long sum2 = simScore.stream()
                .mapToLong(Long::longValue)
                .sum();

        System.out.println(sum2);
    }
}
