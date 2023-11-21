package streamsAsmt;
import java.util.*;

public class StreamFifteenthQ {
	
	public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();
        Trader trader1 = new Trader("Rahul", "Delhi");
        Trader trader2 = new Trader("Amit", "Kolkata");
        Trader trader3 = new Trader("Harish", "Bengaluru");

        transactions.add(new Transaction(trader1, 2011, 300));
        transactions.add(new Transaction(trader2, 2012, 1000));
        transactions.add(new Transaction(trader3, 2011, 400));
        transactions.add(new Transaction(trader1, 2011, 200));
        transactions.add(new Transaction(trader3, 2012, 800));
        
        OptionalInt smallestValue = transactions.stream()
                .mapToInt(Transaction::getValue)
                .min();
        
        if (smallestValue.isPresent()) {
            System.out.println("The smallest transaction value is: " + smallestValue.getAsInt());
        } else {
            System.out.println("No transactions found.");
        }

}
}
