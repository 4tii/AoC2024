import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day14 {
    public static void main(String[] args) throws IOException {
        List<Robot> robots = new ArrayList<>();
        List<Integer> numbers;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day14.txt"))) {
            Pattern pattern = Pattern.compile("-?\\d+");
            numbers = lines.flatMap(line -> {
                Matcher matcher = pattern.matcher(line);
                return matcher.results()
                        .map(result -> Integer.parseInt(result.group()));
            }).toList();
        }
        for(int i=0; i<numbers.size(); i=i+4){
            robots.add(new Robot(new Coordinate(numbers.get(i), numbers.get(i+1)), numbers.get(i+2), numbers.get(i+3)));
        }

        int limitX=101;
        int limitY=103;
        int seconds=10000;
        Set<Coordinate> robotPositions = new HashSet<>();

        for(int i=0; i<seconds; i++) {
            for(Robot robot : robots) {
                robot.position.row = (robot.position.row + robot.xSpeed + limitX) % limitX;
                robot.position.col = (robot.position.col + robot.ySpeed + limitY) % limitY;
                robotPositions.add(robot.position);
            }
            //Part1
            if(i==99)
                System.out.println(countRobotPositionsInQuadrants(robots, limitX, limitY));

            //Part2
            if(robotPositions.size()==robots.size()) {
                System.out.println(i + 1);
                printRobots(limitX, limitY, robots);
            }
            robotPositions.clear();
        }
    }

    private static long countRobotPositionsInQuadrants(List<Robot> robots, int limitX, int limitY){
        int[] quadrantCount = new int[] {0,0,0,0};
        for(Robot robot : robots) {
            if (robot.position.row < limitX / 2) {
                if (robot.position.col < limitY / 2)
                    quadrantCount[0]++;
                else if (robot.position.col > limitY / 2)
                    quadrantCount[2]++;
            } else if (robot.position.row > limitX / 2) {
                if (robot.position.col < limitY / 2)
                    quadrantCount[1]++;
                else if (robot.position.col > limitY / 2)
                    quadrantCount[3]++;
            }
        }
        return Arrays.stream(quadrantCount).reduce(1, (x,y) -> x*y);
    }

    private static void printRobots(int limitX, int limitY, List<Robot> robots){
        char[][] map = new char[limitX][limitY];
        for (int i = 0; i < limitX ; i++) {
            for (int j = 0; j < limitY; j++) {
                map[i][j]=' ';
            }
        }
        for(Robot robot: robots){
            map[robot.position.row][robot.position.col] = 'R';
        }
        for(int i=0; i<limitX; i++){
            for(int j=0; j<limitY; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}

class Robot {
    Coordinate position;
    int xSpeed;
    int ySpeed;

    public Robot(Coordinate position, int xSpeed, int ySpeed) {
        this.position = position;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return xSpeed == robot.xSpeed && ySpeed == robot.ySpeed && Objects.equals(position, robot.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, xSpeed, ySpeed);
    }

    @Override
    public String toString() {
        return "Robot{" +
                "position=" + position +
                ", xSpeed=" + xSpeed +
                ", ySpeed=" + ySpeed +
                '}';
    }
}