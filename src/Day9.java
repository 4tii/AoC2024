import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<Long> input = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./input/Day9.txt"))) {
            char[] inputArray = lines.findFirst().orElse("").toCharArray();
            long id = 0;
            boolean emptySpace = false;
            for(char c : inputArray){
                for(int i=0; i<Long.parseLong(String.valueOf(c)); i++){
                    if(emptySpace)
                        input.add(-1L);
                    else
                        input.add(id);
                }
                if(!emptySpace)
                    id++;
                emptySpace = !emptySpace;
            }
        }

        List<Long> copyInput = new ArrayList<>(input);
        List<Long> reverseList = new ArrayList<>(input.reversed());
        int reverseIdx = 0;
        for(int i=0; i<input.size()-reverseIdx; i++){
            if(input.get(i) == -1L){
                while(reverseList.get(reverseIdx) == -1L)
                    reverseIdx++;
                input.set(i, reverseList.get(reverseIdx));
                input.set(input.size()-1-reverseIdx, -1L);
                reverseIdx++;
            }
        }

        System.out.println(IntStream.range(0, input.size())
                .filter(i -> input.get(i) != -1)
                .mapToLong(i -> i * input.get(i))
                .sum());

        //Part2
        int currentFileSize = 0;
        int currentEmptySize = 0;
        reverseIdx=0;
        while(reverseIdx<reverseList.size()){
            while(reverseList.get(reverseIdx) == -1L)
                reverseIdx++;
            while(reverseIdx<reverseList.size()-1 && reverseList.get(reverseIdx) != -1L && Objects.equals(reverseList.get(reverseIdx), reverseList.get(reverseIdx + 1))){
                reverseIdx++;
                currentFileSize++;
            }
            currentFileSize++;
            for(int j=0; j<copyInput.size()-reverseIdx; j++){
                if(copyInput.get(j) == -1L){
                    currentEmptySize++;
                }else{
                    if(currentEmptySize>=currentFileSize){
                        for(int k=0; k<currentFileSize; k++) {
                            copyInput.set(j-currentEmptySize+k, reverseList.get(reverseIdx+1-currentFileSize+k));
                            copyInput.set(copyInput.size()-1-(reverseIdx+1-currentFileSize+k), -1L);
                        }
                        break;
                    }
                    currentEmptySize=0;
                }
            }
            currentEmptySize=0;
            currentFileSize=0;
            reverseIdx++;
        }

        System.out.println(IntStream.range(0, copyInput.size())
                .filter(i -> copyInput.get(i) != -1)
                .mapToLong(i -> i * copyInput.get(i))
                .sum());
    }
}
