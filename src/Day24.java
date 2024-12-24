import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day24 {
    private static List<Expression> expressions;
    public static void main(String[] args) throws IOException {
        Map<String, Integer> startValues;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day24_1.txt"))) {
            startValues = lines.map(line -> line.split(":"))
                            .collect(Collectors.toMap(
                                parts -> parts[0].trim(),
                                parts -> Integer.parseInt(parts[1].trim())
                            ));
        }
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day24_2.txt"))) {
            expressions = lines.map(line -> {
                            String[] parts = line.split(" ");
                            return new Expression(parts[0], parts[2], parts[1], parts[4]);
                            }).toList();
        }

        int i=0;
        List<Expression> workingList = new ArrayList<>(expressions);
        Map<String, Integer> values = new HashMap<>(startValues);
        while(i<expressions.size()){
            List<Expression> possibleExpressions = workingList.stream().filter(e -> values.containsKey(e.operand1) && values.containsKey(e.operand2)).toList();
            for(Expression e : possibleExpressions){
                switch (e.operation) {
                    case "AND" -> values.put(e.result, values.get(e.operand1) & values.get(e.operand2));
                    case "OR" -> values.put(e.result, values.get(e.operand1) | values.get(e.operand2));
                    case "XOR" -> values.put(e.result, values.get(e.operand1) ^ values.get(e.operand2));
                }
            }
            i = i+possibleExpressions.size();
            workingList.removeAll(possibleExpressions);
        }
        String result = values.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("z"))
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .map(Map.Entry::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining());

        System.out.println(Long.parseLong(result,2));

        //Part 2
        Set<String> wrong = new HashSet<>();
        String carryOver = "";
        for(int j=0; j<startValues.size()/2; j++){
            String x = "x" + (j<10 ? "0" : "") + j;
            String y = "y" + (j<10 ? "0" : "") + j;

            carryOver = checkExpressions(x, y, carryOver, wrong);
        }
        System.out.println(wrong.stream().sorted().collect(Collectors.joining(",")));
    }

    private static String checkExpressions(String op1, String op2, String carryOver, Set<String> wrong){
        Expression temp1 = getExpression(op1, op2, "XOR").orElseThrow();
        if(!temp1.result.equals("z00") && temp1.result.startsWith("z"))
            wrong.add(temp1.result);

        Expression temp2 = getExpression(op1, op2, "AND").orElseThrow();
        if(temp2.result.startsWith("z"))
            wrong.add(temp2.result);

        if(!op1.equals("x00") && !op2.equals("y00")) {
            Optional<Expression> output = getExpression(temp1.result, carryOver, "XOR");
            if(output.isEmpty()) {
                wrong.add(temp1.result);
                wrong.add(temp2.result);
                output = getExpression(temp2.result, carryOver, "XOR");
                if (!output.orElseThrow().result.startsWith("z"))
                    wrong.add(output.get().result);
                return checkCarryOverExpression(temp2.result, temp1.result, output.orElseThrow().result, carryOver, wrong);
            } else if (!output.get().result.startsWith("z"))
                wrong.add(output.get().result);

            if(temp1.result.startsWith("z"))
                return checkCarryOverExpression(output.get().result, temp2.result, temp1.result, carryOver, wrong);
            else if(temp2.result.startsWith("z"))
                return checkCarryOverExpression(temp1.result, output.get().result, temp2.result, carryOver, wrong);
            else
                return checkCarryOverExpression(temp1.result, temp2.result, output.get().result, carryOver, wrong);
        }
        return temp2.result;
    }

    private static String checkCarryOverExpression(String op1, String op2, String output, String carryOver, Set<String> wrong){
        Expression temp3 = getExpression(op1, carryOver, "AND").orElseThrow();
        if(temp3.result.startsWith("z")) {
            wrong.add(temp3.result);
            Expression newCarryOver = getExpression(op2, output, "OR").orElseThrow();
            return newCarryOver.result;
        }else{
            Expression newCarryOver = getExpression(op2, temp3.result, "OR").orElseThrow();
            if (newCarryOver.result.startsWith("z") && !newCarryOver.result.equals("z45")) {
                wrong.add(newCarryOver.result);
                return output;
            }
            return newCarryOver.result;
        }
    }

    private static Optional<Expression> getExpression(String op1, String op2, String operation){
        return expressions.stream().filter(e -> (e.operand1.equals(op1) || e.operand1.equals(op2)) && (e.operand2.equals(op1) || e.operand2.equals(op2)) && e.operation.equals(operation)).findFirst();
    }
}

class Expression {
    String operand1;
    String operand2;
    String operation;
    String result;

    Expression(String operand1, String operand2, String operation, String result) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operation = operation;
        this.result = result;
    }

    @Override
    public String toString() {
        return operand1 + " " + operation + " " + operand2 + " -> " + result;
    }
}
