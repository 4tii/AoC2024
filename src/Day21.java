import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class Day21 {
    public static final Map<Character, Coordinate> numericKeypadCoords = Map.ofEntries(
        entry('7', new Coordinate(0,0)),
        entry('8', new Coordinate(0,1)),
        entry('9', new Coordinate(0,2)),
        entry('4', new Coordinate(1,0)),
        entry('5', new Coordinate(1,1)),
        entry('6', new Coordinate(1,2)),
        entry('1', new Coordinate(2,0)),
        entry('2', new Coordinate(2,1)),
        entry('3', new Coordinate(2,2)),
        entry('X', new Coordinate(3,0)),
        entry('0', new Coordinate(3,1)),
        entry('A', new Coordinate(3,2))
    );

    public static final Map<Character, Coordinate> dirKeypadCoords = Map.ofEntries(
            entry('X', new Coordinate(0,0)),
            entry('^', new Coordinate(0,1)),
            entry('A', new Coordinate(0,2)),
            entry('<', new Coordinate(1,0)),
            entry('v', new Coordinate(1,1)),
            entry('>', new Coordinate(1,2))
    );
    static Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<String> input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day21.txt"))) {
            input = lines.toList();
        }

        solve(input, 3);

        //Part 2
        cache.clear();
        solve(input, 26);
    }

    private static void solve(List<String> input, int robots){
        long sum = 0;
        for(String s: input){
            long result = 0;
            Coordinate cur = numericKeypadCoords.get('A');
            for(char c : s.toCharArray()){
                Coordinate next = numericKeypadCoords.get(c);
                result += findCheapest(cur, next, robots);
                cur=next;
            }
            sum = sum +(result * Long.parseLong(s.substring(0, s.length()-1)));
        }
        System.out.println(sum);
    }

    private static long findCheapest(Coordinate cur, Coordinate next, int robots){
        long result = Long.MAX_VALUE;
        Queue<PathState> queue = new LinkedList<>();
        queue.add(new PathState(cur, ""));

        while (!queue.isEmpty()) {
            PathState curState = queue.poll();
            if (curState.currCoord.equals(next)) {
                long temp = doRobot(curState.path + "A", robots);
                result = Math.min(temp, result);
            } else if (!curState.currCoord.equals(numericKeypadCoords.get('X'))){
                if(curState.currCoord.row > next.row)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row-1, curState.currCoord.col), curState.path + "^"));
                else if(curState.currCoord.row < next.row)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row+1, curState.currCoord.col), curState.path + "v"));
                if(curState.currCoord.col > next.col)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row, curState.currCoord.col-1), curState.path + "<"));
                else if(curState.currCoord.col < next.col)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row, curState.currCoord.col+1), curState.path + ">"));
            }
        }
        return result;
    }

    private static long doRobot(String path, int robots){
        if(robots == 1)
            return path.length();

        long result = 0;
        Coordinate cur = dirKeypadCoords.get('A');

        for(char c: path.toCharArray()){
            Coordinate next = dirKeypadCoords.get(c);
            result += findCheapestDirectional(cur, next, robots);
            cur = next;
        }
        return result;
    }

    private static long findCheapestDirectional(Coordinate cur, Coordinate next, int robots){
        String key = cur + ";" + next + ";" + robots;
        if(cache.containsKey(key))
            return cache.get(key);

        long result = Long.MAX_VALUE;
        Queue<PathState> queue = new LinkedList<>();
        queue.add(new PathState(cur, ""));

        while (!queue.isEmpty()) {
            PathState curState = queue.poll();
            if (curState.currCoord.equals(next)) {
                long temp = doRobot(curState.path + "A", robots - 1);
                result = Math.min(temp, result);
            } else if (!curState.currCoord.equals(dirKeypadCoords.get('X'))){
                if(curState.currCoord.row > next.row)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row-1, curState.currCoord.col), curState.path + "^"));
                else if(curState.currCoord.row < next.row)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row+1, curState.currCoord.col), curState.path + "v"));
                if(curState.currCoord.col > next.col)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row, curState.currCoord.col-1), curState.path + "<"));
                else if(curState.currCoord.col < next.col)
                    queue.add(new PathState(new Coordinate(curState.currCoord.row, curState.currCoord.col+1), curState.path + ">"));
            }
        }
        cache.put(key, result);
        return result;
    }
}

class PathState {
    Coordinate currCoord;
    String path;

    public PathState(Coordinate currCoord, String path) {
        this.currCoord = currCoord;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PathState pathState = (PathState) o;
        return Objects.equals(currCoord, pathState.currCoord) && Objects.equals(path, pathState.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currCoord, path);
    }
}
