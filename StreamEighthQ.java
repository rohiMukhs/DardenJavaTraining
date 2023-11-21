package streamsAsmt;
import java.util.*;
import java.util.stream.Collectors;

public class StreamEighthQ {

	public static void main(String[] args) {
		
		 	List<Transaction> transactions = new ArrayList<>();
	        Trader trader1 = new Trader("Rahul", "Mumbai");
	        Trader trader2 = new Trader("Amit", "Kolkata");
	        Trader trader3 = new Trader("Harish", "Bengaluru");

	        transactions.add(new Transaction(trader1, 2011, 300));
	        transactions.add(new Transaction(trader2, 2012, 1000));
	        transactions.add(new Transaction(trader3, 2011, 400));
	        transactions.add(new Transaction(trader1, 2011, 200));

	        List<Transaction> result = transactions.stream()
	                .filter(transaction -> transaction.getYear() == 2011)
	                .sorted(Comparator.comparingInt(Transaction::getValue))
	                .collect(Collectors.toList());

	        System.out.println("Transactions in 2011 sorted by value (low to high):");
	        result.forEach(transaction ->
	        System.out.println("Value: " + transaction.getValue() + ", Trader: " + transaction.trader.name));
		

	}

}

class Trader {
    String name;
    String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getName() {
        return name;
    }
}

class Transaction {
    Trader trader;
    int year;
    int value;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
    
    public Trader getTrader() {
        return trader;
    }

    public int getYear() {
        return year;
    }

    public int getValue() {
        return value;
    }
}
