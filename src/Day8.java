import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<String> input;
        Map<Character, List<Coordinate>> senders;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day8.txt"))) {
            input = lines.toList();
            senders = IntStream.range(0, input.size())
                    .boxed()
                    .flatMap(row -> {
                        String line = input.get(row);
                        return IntStream.range(0, line.length())
                                .filter(col -> line.charAt(col) != '.')
                                .mapToObj(col -> Map.entry(line.charAt(col), new Coordinate(row, col)));
                    })
                    .collect(Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                    ));
        }

        Set<Coordinate> antiNodes = new HashSet<>();
        for (var entry : senders.entrySet()) {
            for(int i=0; i<entry.getValue().size()-1; i++){
                for(int j=i+1; j<entry.getValue().size(); j++){
                    Coordinate c1 = entry.getValue().get(i);
                    Coordinate c2 = entry.getValue().get(j);
                    int distRow = c1.row - c2.row;
                    int distCol = c1.col - c2.col;
                    addIfIsInMap(c1, distRow, distCol, input, antiNodes);
                    addIfIsInMap(c2, distRow*-1, distCol*-1, input, antiNodes);
                }
            }
        }
        System.out.println(antiNodes.size());

        //Part 2
        antiNodes.clear();
        for (var entry : senders.entrySet()) {
            for(int i=0; i<entry.getValue().size()-1; i++){
                for(int j=i+1; j<entry.getValue().size(); j++){
                    Coordinate c1 = entry.getValue().get(i);
                    Coordinate c2 = entry.getValue().get(j);
                    int distRow = c1.row - c2.row;
                    int distCol = c1.col - c2.col;
                    addIfIsInMapWithHarmonics(c1, distRow, distCol, input, antiNodes);
                    addIfIsInMapWithHarmonics(c2, distRow*-1, distCol*-1, input, antiNodes);
                }
            }
        }
        System.out.println(antiNodes.size());
    }

    public static void addIfIsInMap(Coordinate c, int distRow, int distCol, List<String> input, Set<Coordinate> antiNodes){
        if(c.row + distRow >= 0 && c.row + distRow < input.size() && c.col + distCol >=0 && c.col + distCol < input.get(0).length())
            antiNodes.add(new Coordinate(c.row + distRow, c.col + distCol));
    }

    public static void addIfIsInMapWithHarmonics(Coordinate c, int distRow, int distCol, List<String> input, Set<Coordinate> antiNodes){
        antiNodes.add(c);
        while(c.row + distRow >= 0 && c.row + distRow < input.size() && c.col + distCol >=0 && c.col + distCol < input.get(0).length()){
            antiNodes.add(new Coordinate(c.row + distRow, c.col + distCol));
            c = new Coordinate(c.row + distRow, c.col + distCol);
        }
    }
}

class Coordinate {
    int row;
    int col;

    Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate state = (Coordinate) o;
        return row == state.row && col == state.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return row + "," + col;
    }
}