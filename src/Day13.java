import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day13 {
    public static void main(String[] args) throws IOException {
        List<Integer> numbers;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day13.txt"))) {
            Pattern pattern = Pattern.compile("-?\\d+");
            numbers = lines.flatMap(line -> {
                        Matcher matcher = pattern.matcher(line);
                        return matcher.results()
                                .map(result -> Integer.parseInt(result.group()));
                    }).toList();
        }

        long sum = 0;
        for (int i = 0; i < numbers.size(); i = i + 6) {
            long[] buttonA = new long[]{numbers.get(i), numbers.get(i + 1)};
            long[] buttonB = new long[]{numbers.get(i + 2), numbers.get(i + 3)};
            long[] target = new long[]{numbers.get(i + 4), numbers.get(i + 5)};
            long[] presses = solve(buttonA, buttonB, target);
            sum = sum + presses[0] * 3 + presses[1];
        }
        System.out.println(sum);

        //Part 2
        sum = 0;
        for (int i = 0; i < numbers.size(); i = i + 6) {
            long[] buttonA = new long[]{numbers.get(i), numbers.get(i + 1)};
            long[] buttonB = new long[]{numbers.get(i + 2), numbers.get(i + 3)};
            long[] target = new long[]{numbers.get(i + 4) + 10000000000000L, numbers.get(i + 5) + 10000000000000L};
            long[] presses = solve(buttonA, buttonB, target);
            sum = sum + presses[0] * 3 + presses[1];
        }
        System.out.println(sum);

    }

    public static long[] solve(long[] buttonA, long[] buttonB, long[] target) {
        double n2 = (double) (target[1] * buttonA[0] - target[0] * buttonA[1]) / (double) (buttonA[0] * buttonB[1] - buttonB[0] * buttonA[1]);
        double n1 = (target[0] - n2 * buttonB[0]) / buttonA[0];
        if (n2 == (long) n2 && n1 == (long) n1) {
            return new long[]{(long) n1, (long) n2};
        }
        return new long[]{0, 0};
    }
}