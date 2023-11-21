package streamsAsmt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTenthQ {

	public static void main(String[] args) {
		
		List<Trader> traders = new ArrayList<>();
        traders.add(new Trader("Rahul", "Mumbai"));
        traders.add(new Trader("Amit", "Kolkata"));
        traders.add(new Trader("Harish", "Pune"));
        traders.add(new Trader("Naman", "Kolkata"));
        traders.add(new Trader("Dharmesh", "Bengaluru"));
        traders.add(new Trader("Rohit", "Pune"));
        traders.add(new Trader("Kumar", "Pune"));

        
        
        List<Trader> tradersInPuneSortedByName = traders.stream()
                .filter(trader -> trader.getCity().equalsIgnoreCase("Pune"))
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());

        System.out.println("Traders from Pune sorted by name:");
        tradersInPuneSortedByName.forEach(trader -> System.out.println(trader.getName()));

	}

}
