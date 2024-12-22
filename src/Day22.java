import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Day22 {
    public static List<Map<Integer, Integer>> sequencesList = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        List<Long> input;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day22.txt"))) {
            input = lines.map(Long::parseLong).toList();
        }


        long sum = 0;
        for(Long value : input){
            List<Integer> window = new ArrayList<>();
            Map<Integer, Integer> sequenceMap = new HashMap<>();
            for(int i=0; i<2000; i++){
                long prev = value;
                long temp = value << 6;
                temp = temp ^ value;
                value = temp & 16777215;
                temp = value >> 5;
                temp = temp ^ value;
                value = temp & 16777215;
                temp = value << 11;
                temp = temp ^ value;
                value = temp & 16777215;

                if(window.size()==4)
                    window.removeFirst();
                window.add((int) ((value%10) - (prev%10)));

                if(window.size()==4) {
                    if(!sequenceMap.containsKey(window.hashCode()))
                        sequenceMap.put(window.hashCode(), (int) (value%10));
                }
            }
            sequencesList.add(sequenceMap);
            sum += value;
        }
        System.out.println(sum);

        //Part 2
        List<Solver> solvers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for(int i=-9; i<=9; i++){
            Solver s = new Solver(i);
            Thread thread = new Thread(s);
            solvers.add(s);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int maxBananas = solvers.stream().mapToInt(Solver::getMaxBananas).max().orElseThrow();
        System.out.println(maxBananas);
    }

    public static class Solver implements Runnable {
        private final int first;
        int maxBananas;

        public Solver(int first){
            this.first = first;
            maxBananas=0;
        }

        @Override
        public void run() {
            for(int j=-9; j<= 9; j++){
                for(int k=-9; k<=9; k++){
                    for(int l=-9; l<=9; l++){
                        int temp = 0;
                        List<Integer> list = Arrays.asList(first, j, k, l);
                        for(Map<Integer, Integer> map: sequencesList){
                            temp += map.getOrDefault(list.hashCode(), 0);
                        }
                        maxBananas = Math.max(maxBananas, temp);
                    }
                }
            }
        }

        public int getMaxBananas(){
            return maxBananas;
        }
    }
}


