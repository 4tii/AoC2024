import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day5 {
    public static void main(String[] args) throws IOException {
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day5_1.txt"))) {
            lines
                .map(line -> line.split("\\|"))
                .forEach(parts -> {
                    if(map.get(Integer.parseInt(parts[0]))!=null) {
                        List<Integer> list = map.get(Integer.parseInt(parts[0]));
                        list.add(Integer.parseInt(parts[1]));
                    } else {
                        List<Integer> list = new ArrayList<>();
                        list.add(Integer.parseInt(parts[1]));
                        map.put(Integer.parseInt(parts[0]), list);
                    }
                });
        }
        List<List<Integer>> list = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day5_2.txt"))) {
            lines
                .map(line -> line.split(","))
                .forEach(parts -> {
                    List<Integer> l = new ArrayList<>();
                    Arrays.stream(parts).forEach(part -> l.add(Integer.parseInt(part)));
                    list.add(l);
                });
        }

        int sum = 0;
        List<Integer> correctIdx = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            for(int j=0; j<list.get(i).size()-1; j++) {
                if(map.get(list.get(i).get(j+1)).contains(list.get(i).get(j))) {
                    break;
                }
                if(j==(list.get(i).size()-2)) {
                    sum += list.get(i).get(list.get(i).size() / 2);
                    correctIdx.add(i);
                }
            }
        }
        System.out.println(sum);

        //Part 2
        sum=0;
        for (List<Integer> integers : list) {
            integers.sort((a, b) -> {
                if (map.get(a).contains(b))
                    return -1;
                return 1;
            });
        }
        for(int i=0; i<list.size(); i++) {
            if(!correctIdx.contains(i)) {
                sum += list.get(i).get(list.get(i).size() / 2);
            }
        }
        System.out.println(sum);
    }
}
