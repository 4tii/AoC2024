import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 {
    public static void main(String[] args) throws IOException {
        Map<String, Set<String>> map;
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day23.txt"))) {
            map = lines.flatMap(line -> {
                String[] parts = line.split("-");
                return Stream.of(
                    Map.entry(parts[0], parts[1]),
                    Map.entry(parts[1], parts[0])
                );
                }).collect(Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
                ));
        }

        Set<Set<String>> setsOfThree = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            for(String s : entry.getValue()){
                for(String s2 : map.get(s)){
                    if(map.get(s2).contains(entry.getKey())) {
                        setsOfThree.add(new TreeSet<>(Arrays.asList(entry.getKey(), s, s2)));
                    }
                }
            }
        }
        System.out.println(setsOfThree.stream().filter(l -> l.stream().anyMatch(s -> s.startsWith("t"))).count());

        //Part 2
        Set<String> largest = findLargestClique(map);
        String password = largest.stream().sorted().collect(Collectors.joining(","));
        System.out.println(password);
    }

    private static Set<String> findLargestClique(Map<String, Set<String>> map){
        Set<String> l = new HashSet<>();
        Set<String> r = new HashSet<>();
        Set<String> p = new HashSet<>(map.keySet());
        Set<String> x = new HashSet<>();

        bronKerbosch(map, r, p, x, l);
        return l;
    }

    private static void bronKerbosch(Map<String, Set<String>> map, Set<String> r, Set<String> p, Set<String> x, Set<String> l){
        if (p.isEmpty() && x.isEmpty()) {
            if (r.size() > l.size()) {
                l.clear();
                l.addAll(r);
            }
            return;
        }

        for (String pc : new HashSet<>(p)) {
            r.add(pc);
            Set<String> newP = intersect(p, map.getOrDefault(pc, Set.of()));
            Set<String> newX = intersect(x, map.getOrDefault(pc, Set.of()));

            bronKerbosch(map, r, newP, newX, l);

            r.remove(pc);
            p.remove(pc);
            x.add(pc);
        }
    }

    private static Set<String> intersect(Set<String> set1, Set<String> set2) {
        Set<String> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }
}
