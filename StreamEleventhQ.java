package streamsAsmt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamEleventhQ {

	public static void main(String[] args) {
		
		List<Trader> traders = new ArrayList<>();
        traders.add(new Trader("Rahul", "Mumbai"));
        traders.add(new Trader("Amit", "Kolkata"));
        traders.add(new Trader("Harish", "Pune"));
        traders.add(new Trader("Naman", "Kolkata"));
        traders.add(new Trader("Dharmesh", "Bengaluru"));
        traders.add(new Trader("Rohit", "Pune"));
        traders.add(new Trader("Kumar", "Pune"));
        
        String sortedNames = traders.stream()
                .map(Trader::getName)
                .sorted()
                .collect(Collectors.joining(", "));

        System.out.println("All traders' names sorted alphabetically: " + sortedNames);

	}

}
