import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class Day15 {
    private static final int[][] DIRECTIONS = {
            {-1, 0},  // Up
            {0, 1},   // Right
            {1, 0},   // Down
            {0, -1},  // Left
    };

    public static Map<Character, int[]> directionMap = Map.ofEntries(
            entry('^', DIRECTIONS[0]),
            entry('>', DIRECTIONS[1]),
            entry('v', DIRECTIONS[2]),
            entry('<', DIRECTIONS[3])
    );

    public static void main(String[] args) throws IOException {
        char[][] map;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day15_1.txt"))) {
            map = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        char[][] resizedMap = resizeMap(map);
        List<Character> movements;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day15_2.txt"))) {
            movements = lines.flatMapToInt(String::chars).mapToObj(c -> (char) c).toList();
        }
        int positionX = 0;
        int positionY = 0;
        for (int i=0; i<map.length; i++) {
            for(int j=0; j<map[i].length; j++){
                if(map[i][j]=='@') {
                    positionX = i;
                    positionY = j;
                }
            }
        }

        //Part 1
        moveRobot1(map, movements, positionX, positionY);
        long sum=0;
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                if(map[i][j]=='O'){
                    sum = sum + (i* 100L) + j;
                }
            }
        }
        System.out.println(sum);

        //Part 2
        map = resizedMap;
        sum=0;
        positionX = 0;
        positionY = 0;
        for (int i=0; i<map.length; i++) {
            for(int j=0; j<map[i].length; j++){
                if(map[i][j]=='@') {
                    positionX = i;
                    positionY = j;
                }
            }
        }
        moveRobot2(map, movements, positionX, positionY);
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                if(map[i][j]=='['){
                    sum = sum + (i * 100L) + j;
                }
            }
        }
        System.out.println(sum);
    }

    private static void moveRobot1(char[][] map, List<Character> movements, int positionX, int positionY){
        for(Character movement : movements){
            int newPositionX = positionX + directionMap.get(movement)[0];
            int newPositionY = positionY + directionMap.get(movement)[1];
            if(map[newPositionX][newPositionY]=='.'){
                map[positionX][positionY] = '.';
                map[newPositionX][newPositionY] = '@';
                positionX = newPositionX;
                positionY = newPositionY;
            } else if(map[newPositionX][newPositionY]=='O') {
                int x=0;
                int y=0;
                while(map[newPositionX+x][newPositionY+y]=='O'){
                    x = x + directionMap.get(movement)[0];
                    y = y + directionMap.get(movement)[1];
                }
                if(map[newPositionX+x][newPositionY+y]=='.'){
                    while(x!=0 || y!=0){
                        map[newPositionX+x][newPositionY+y] = 'O';
                        x = x - directionMap.get(movement)[0];
                        y = y - directionMap.get(movement)[1];
                    }
                    map[positionX][positionY] = '.';
                    map[newPositionX][newPositionY] = '@';
                    positionX = newPositionX;
                    positionY = newPositionY;
                }
            }
        }
    }

    private static void moveRobot2(char[][] map, List<Character> movements, int positionX, int positionY){
        for(Character movement : movements){
            int newPositionX = positionX + directionMap.get(movement)[0];
            int newPositionY = positionY + directionMap.get(movement)[1];
            if(map[newPositionX][newPositionY]=='.'){
                map[positionX][positionY] = '.';
                map[newPositionX][newPositionY] = '@';
                positionX = newPositionX;
                positionY = newPositionY;
            } else if(map[newPositionX][newPositionY]=='[' || map[newPositionX][newPositionY]==']') {
                if(directionMap.get(movement)==DIRECTIONS[1] || directionMap.get(movement)==DIRECTIONS[3]) {
                    int x = 0;
                    int y = 0;
                    while (map[newPositionX + x][newPositionY + y] == '[' || map[newPositionX + x][newPositionY + y] == ']') {
                        x = x + 2*directionMap.get(movement)[0];
                        y = y + 2*directionMap.get(movement)[1];
                    }
                    if (map[newPositionX + x][newPositionY + y] == '.') {
                        while (x != 0 || y != 0) {
                            if(directionMap.get(movement)==DIRECTIONS[1]){
                                map[newPositionX + x][newPositionY + y-1] = '[';
                                map[newPositionX + x][newPositionY + y] = ']';
                            } else {
                                map[newPositionX + x][newPositionY + y] = '[';
                                map[newPositionX + x][newPositionY + y+1] = ']';
                            }
                            x = x - 2*directionMap.get(movement)[0];
                            y = y - 2*directionMap.get(movement)[1];
                        }
                        map[positionX][positionY] = '.';
                        map[newPositionX][newPositionY] = '@';
                        positionX = newPositionX;
                        positionY = newPositionY;
                    }
                } else {
                    List<Box> boxesToPush = new ArrayList<>();
                    Box box;
                    if(map[newPositionX][newPositionY]=='[')
                        box = new Box(new Coordinate(newPositionX, newPositionY), new Coordinate(newPositionX, newPositionY+1));
                    else
                        box = new Box(new Coordinate(newPositionX, newPositionY-1), new Coordinate(newPositionX, newPositionY));
                    boxesToPush.add(box);
                    if(canPush(boxesToPush, map, box, directionMap.get(movement))){
                        pushBoxes(boxesToPush, map, directionMap.get(movement));
                        map[newPositionX][newPositionY] = '@';
                        map[positionX][positionY] = '.';
                        positionX = newPositionX;
                        positionY = newPositionY;
                    }
                }
            }
        }
    }

    private static boolean canPush(List<Box> boxesToPush, char[][] map, Box boxToCheck, int[] currentDir){
        if(map[boxToCheck.left.row + currentDir[0]][boxToCheck.left.col] == '#' ||
                map[boxToCheck.right.row + currentDir[0]][boxToCheck.right.col] == '#')
            return false;

        boolean canPush = true;
        if(map[boxToCheck.left.row + currentDir[0]][boxToCheck.left.col] == '['){
            Box box = new Box(new Coordinate(boxToCheck.left.row + currentDir[0], boxToCheck.left.col), new Coordinate(boxToCheck.right.row + currentDir[0], boxToCheck.right.col));
            boxesToPush.add(box);
            canPush &= canPush(boxesToPush, map, box, currentDir);
        }
        if(map[boxToCheck.left.row + currentDir[0]][boxToCheck.left.col] == ']'){
            Box box = new Box(new Coordinate(boxToCheck.left.row + currentDir[0], boxToCheck.left.col-1), new Coordinate(boxToCheck.left.row + currentDir[0], boxToCheck.left.col));
            boxesToPush.add(box);
            canPush &= canPush(boxesToPush, map, box, currentDir);
        }
        if(map[boxToCheck.right.row + currentDir[0]][boxToCheck.right.col] == '['){
            Box box = new Box(new Coordinate(boxToCheck.right.row + currentDir[0], boxToCheck.right.col), new Coordinate(boxToCheck.right.row + currentDir[0], boxToCheck.right.col+1));
            boxesToPush.add(box);
            canPush &= canPush(boxesToPush, map, box, currentDir);
        }
        return canPush;
    }

    private static void pushBoxes(List<Box> boxesToPush, char[][] map, int[] currentDir){
        boxesToPush.sort((a,b) -> {
            if(a.left.row > b.left.row)
                return currentDir[0]*-1;
            else if(a.left.row == b.left.row)
                return 0;
            else
                return currentDir[0];
        });

        for(Box box : boxesToPush){
            map[box.left.row+currentDir[0]][box.left.col] = '[';
            map[box.right.row+currentDir[0]][box.right.col] = ']';
            map[box.left.row][box.left.col] = '.';
            map[box.right.row][box.right.col] = '.';
        }
    }

    private static char[][] resizeMap(char[][] map){
        char[][] newMap = new char[map.length][2*map[0].length];
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                if(map[i][j]=='#') {
                    newMap[i][2*j] = '#';
                    newMap[i][2*j+1] = '#';
                } else if(map[i][j]=='O') {
                    newMap[i][2*j] = '[';
                    newMap[i][2*j+1] = ']';
                } else if(map[i][j]=='.') {
                    newMap[i][2*j] = '.';
                    newMap[i][2*j+1] = '.';
                } else if(map[i][j]=='@') {
                    newMap[i][2*j] = '@';
                    newMap[i][2*j+1] = '.';
                }
            }
        }
        return newMap;
    }
}

class Box{
    Coordinate left;
    Coordinate right;

    public Box(Coordinate left, Coordinate right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Objects.equals(left, box.left) && Objects.equals(right, box.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
