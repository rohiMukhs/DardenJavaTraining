package streamsAsmt;
import java.util.*;
import java.util.stream.Collectors;

public class StreamNinthQ {

	public static void main(String[] args) {
		
		List<Trader> traders = new ArrayList<>();
        traders.add(new Trader("Rahul", "Mumbai"));
        traders.add(new Trader("Amit", "Kolkata"));
        traders.add(new Trader("Harish", "Bengaluru"));
        traders.add(new Trader("Naman", "Kolkata"));
        traders.add(new Trader("Dharmesh", "Mumbai"));
        
        Set<String> uniqueCities = traders.stream()
                .map(Trader::getCity)
                .collect(Collectors.toSet());

        System.out.println("Unique cities where traders work: " + uniqueCities);
	}

}
