package streamsAsmt;
import java.util.*;

public class StreamThirteenthQ {

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

	        System.out.println("Transaction values from traders in Delhi:");
	        transactions.stream()
	                .filter(transaction -> transaction.getTrader().getCity().equalsIgnoreCase("Delhi"))
	                .map(Transaction::getValue)
	                .forEach(System.out::println);
	    }

}
