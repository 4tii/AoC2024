import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17 {
        public static void main(String[] args) throws IOException {
        List<Integer> numbers;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day17.txt"))) {
            Pattern pattern = Pattern.compile("-?\\d+");
            numbers = lines.flatMap(line -> {
                Matcher matcher = pattern.matcher(line);
                return matcher.results()
                        .map(result -> Integer.parseInt(result.group()));
            }).toList();
        }
        long registerA = numbers.get(0);
        long registerB = numbers.get(1);
        long registerC = numbers.get(2);

        List<Integer> operations = numbers.subList(3, numbers.size());
        List<Integer> expected = new ArrayList<>(operations.reversed());

        System.out.println(runProgramm(operations, registerA, registerB, registerC).stream().map(String::valueOf).collect(Collectors.joining(",")));

        //Part 2
        System.out.println(findCorrectRegisterValue(operations, 0, registerB, registerC, 0, expected));
    }

    public static long getComboValue(int value, long registerA, long registerB, long registerC) {
        if(value>=0 && value<=3)
            return value;
        else if(value==4)
            return registerA;
        else if(value==5)
            return registerB;
        else if(value==6)
            return registerC;
        return -1;
    }

    public static List<Integer> runProgramm(List<Integer> operations, long registerA, long registerB, long registerC){
        int pc = 0;
        List<Integer> result = new ArrayList<>();
        while (pc < operations.size()) {
            int op = operations.get(pc);
            int value = operations.get(pc + 1);
            if (op == 0) {
                registerA = (long) (registerA / Math.pow(2,getComboValue(value, registerA, registerB, registerC)));
            } else if(op == 1) {
                registerB = registerB ^ value;
            } else if(op == 2) {
                registerB = getComboValue(value, registerA, registerB, registerC) % 8;
            } else if(op == 3) {
                if(registerA!=0)
                    pc = value;
            } else if(op == 4) {
                registerB = registerB ^ registerC;
            } else if(op == 5) {
                result.add((int) (getComboValue(value, registerA, registerB, registerC) % 8));
            } else if(op == 6) {
                registerB = (long) (registerA / Math.pow(2,getComboValue(value, registerA, registerB, registerC)));
            } else if(op == 7) {
                registerC = (long) (registerA / Math.pow(2,getComboValue(value, registerA, registerB, registerC)));
            }
            if(op!=3 || registerA==0)
                pc=pc+2;
        }
        return result;
    }

    public static long findCorrectRegisterValue(List<Integer> operations, long registerA, long registerB, long registerC, int depth, List<Integer> expected){
        if(depth==operations.size())
            return registerA;
        
        for(int num=0; num<=8; num++){
            List<Integer> subProgramm = runProgramm(operations, (registerA << 3) + num, registerB, registerC);
            if(!subProgramm.isEmpty() && Objects.equals(subProgramm.getFirst(), expected.get(depth)) && (depth>0 || num!=0)) {
                long value = findCorrectRegisterValue(operations, (registerA << 3) + num, registerB, registerC, depth + 1, expected);
                if (value != 0)
                    return value;
                }
        }
        return 0;
    }
}
