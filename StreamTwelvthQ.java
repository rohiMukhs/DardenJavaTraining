package streamsAsmt;

import java.util.ArrayList;
import java.util.List;

public class StreamTwelvthQ {

	public static void main(String[] args) {
		
		List<Trader> traders = new ArrayList<>();
        traders.add(new Trader("Rahul", "Mumbai"));
        traders.add(new Trader("Amit", "Kolkata"));
        traders.add(new Trader("Harish", "Pune"));
        traders.add(new Trader("Naman", "Kolkata"));
        traders.add(new Trader("Dharmesh", "Bengaluru"));
        traders.add(new Trader("Rohit", "Indore"));
        traders.add(new Trader("Kumar", "Pune"));
        
        boolean anyTraderInIndore = traders.stream()
                .anyMatch(trader -> trader.getCity().equalsIgnoreCase("Indore"));

        if (anyTraderInIndore) {
            System.out.println("There are traders based in Indore.");
        } else {
            System.out.println("No traders are based in Indore.");
        }

	}

}
