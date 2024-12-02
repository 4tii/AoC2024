import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<List<Integer>> input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day2.txt"))) {
            input = lines
                .map(line -> Arrays.stream(line.split(" "))
                        .map(Integer::parseInt).toList())
                    .toList();
        }

        int safe=input.size();
        for(List<Integer> list : input) {
            safe = getSafe(safe, list);
        }
        System.out.println(safe);

        //Part 2
        safe=input.size();
        for(List<Integer> list : input) {
            for(int i=0; i<list.size()-1; i++) {
                //decreasing
                if(list.get(0)>list.get(1)){
                    if(list.get(i)<list.get(i+1) || list.get(i)-list.get(i+1)>3 || list.get(i)-list.get(i+1)==0) {
                        if(checkUnsafeWithRemoving(list))
                            safe--;
                        break;
                    }
                } else { //increasing
                    if(list.get(i)>list.get(i+1) || list.get(i+1)-list.get(i)>3 || list.get(i)-list.get(i+1)==0) {
                        if(checkUnsafeWithRemoving(list))
                            safe--;
                        break;
                    }
                }
            }
        }
        System.out.println(safe);
    }

    private static int getSafe(int safe, List<Integer> list) {
        for(int i=0; i<list.size()-1; i++) {
            //decreasing
            if(list.get(0)>list.get(1)){
                if(list.get(i)<list.get(i+1) || list.get(i)-list.get(i+1)>3 || list.get(i)-list.get(i+1)==0) {
                    safe--;
                    break;
                }
            } else { //increasing
                if(list.get(i)>list.get(i+1) || list.get(i+1)-list.get(i)>3 || list.get(i)-list.get(i+1)==0) {
                    safe--;
                    break;
                }
            }
        }
        return safe;
    }

    public static boolean checkUnsafeWithRemoving(List<Integer> input) {
        int safe=input.size();
        for(int j=0; j<input.size(); j++) {
            List<Integer> list = new ArrayList<>(input);
            list.remove(j);
            safe = getSafe(safe, list);
        }
        return safe==0;
    }


}
