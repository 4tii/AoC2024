import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<Long> sums = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day3.txt"))) {
            String pattern = "mul\\(\\d{1,3},\\d{1,3}\\)";
            Pattern compiledPattern = Pattern.compile(pattern);
            lines.forEach(s -> {
                Matcher matcher = compiledPattern.matcher(s);
                long sum = 0;
                while (matcher.find()) {
                    String instruction = matcher.group();
                    Long v1 = Long.parseLong(instruction.substring(instruction.indexOf("(")+1, instruction.indexOf(",")));
                    Long v2 = Long.parseLong(instruction.substring(instruction.indexOf(",")+1, instruction.indexOf(")")));
                    sum += (v1*v2);
                }
                sums.add(sum);
            });
            System.out.println(sums.stream().mapToLong(Long::longValue).sum());
        }

        //Part 2
        List <Long> sums2 = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day3.txt"))) {
            String pattern = "mul\\(\\d{1,3},\\d{1,3}\\)|don't\\(\\)|do\\(\\)";
            Pattern compiledPattern = Pattern.compile(pattern);
            AtomicBoolean enabled = new AtomicBoolean(true);
            lines.forEach(s -> {
                Matcher matcher = compiledPattern.matcher(s);
                long sum = 0;
                while (matcher.find()) {
                    String instruction = matcher.group();
                    if(instruction.equals("don't()"))
                        enabled.set(false);
                    else if (instruction.equals("do()"))
                        enabled.set(true);

                    if(enabled.get() && instruction.startsWith("mul")) {
                        Long v1 = Long.parseLong(instruction.substring(instruction.indexOf("(") + 1, instruction.indexOf(",")));
                        Long v2 = Long.parseLong(instruction.substring(instruction.indexOf(",") + 1, instruction.indexOf(")")));
                        sum += (v1 * v2);
                    }
                }
                sums2.add(sum);
            });
            System.out.println(sums2.stream().mapToLong(Long::longValue).sum());
        }
    }
}
