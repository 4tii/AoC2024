import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 {
    public static void main(String[] args) throws IOException {
        char[][] map;
        List<Coordinate> zeros;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day10.txt"))) {
            map = lines.map(String::toCharArray).toArray(char[][]::new);
            zeros = IntStream.range(0, map.length)
                    .boxed()
                    .flatMap(row -> {
                        char[] line = map[row];
                        return IntStream.range(0, line.length)
                                .filter(col -> line[col] == '0')
                                .mapToObj(col -> new Coordinate(row, col));
                    }).toList();
        }
        Map<Coordinate, Set<Coordinate>> nines = new HashMap<>();
        for(Coordinate zero: zeros) {
            Set<Coordinate> set = new HashSet<>();
            nines.put(zero, set);
            canReachDifferentTops(map, zero.row, zero.col, nines, zero);
        }
        System.out.println(nines.values()
                .stream()
                .mapToInt(Set::size)
                .sum());

        //Part 2
        int sum=0;
        for(Coordinate zero: zeros) {
            sum = canReachTopOverDifferntPaths(map, zero.row, zero.col, sum);
        }
        System.out.println(sum);
    }

    public static void canReachDifferentTops(char[][] map, int x, int y, Map<Coordinate, Set<Coordinate>> nines, Coordinate start){
        if(map[x][y] == '9'){
            nines.get(start).add(new Coordinate(x, y));
            return;
        }
        if(x-1 >= 0 && checkSlope(map, x, x-1, y, true))
            canReachDifferentTops(map, x-1, y, nines, start);
        if(y+1 < map[0].length && checkSlope(map, y, y+1, x, false))
            canReachDifferentTops(map, x, y+1, nines, start);
        if(x+1 < map.length && checkSlope(map, x, x+1, y, true))
            canReachDifferentTops(map, x+1, y, nines, start);
        if(y-1 >= 0 && checkSlope(map, y, y-1, x, false))
            canReachDifferentTops(map, x, y-1, nines, start);
    }

    public static int canReachTopOverDifferntPaths(char[][] map, int x, int y, int sum){
        if(map[x][y] == '9'){
            return ++sum;
        }
        if(x-1 >= 0 && checkSlope(map, x, x-1, y, true))
            sum = canReachTopOverDifferntPaths(map, x-1, y, sum);
        if(y+1 < map[0].length && checkSlope(map, y, y+1, x, false))
            sum = canReachTopOverDifferntPaths(map, x, y+1, sum);
        if(x+1 < map.length && checkSlope(map, x, x+1, y, true))
            sum = canReachTopOverDifferntPaths(map, x+1, y, sum);
        if(y-1 >= 0 && checkSlope(map, y, y-1, x, false))
            sum = canReachTopOverDifferntPaths(map, x, y-1, sum);

        return sum;
    }

    public static boolean checkSlope(char[][] map, int oldValue, int newValue, int constantValue, boolean isX){
        if(isX)
            return Integer.parseInt(String.valueOf(map[newValue][constantValue])) - Integer.parseInt(String.valueOf(map[oldValue][constantValue])) == 1;
        else
            return Integer.parseInt(String.valueOf(map[constantValue][newValue])) - Integer.parseInt(String.valueOf(map[constantValue][oldValue])) == 1;
    }
}
