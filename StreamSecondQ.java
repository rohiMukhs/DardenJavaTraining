package streamsAsmt;
import java.util.*;

public class StreamSecondQ {

	public static void main(String[] args) {
		
		 List<Fruit> fruits = new ArrayList<>();
	        fruits.add(new Fruit("Apple", 95, 50, "Red"));
	        fruits.add(new Fruit("Orange", 45, 40, "Orange"));
	        fruits.add(new Fruit("Banana", 105, 30, "Yellow"));
	        fruits.add(new Fruit("Grapes", 80, 60, "Purple"));
	        fruits.add(new Fruit("Pear", 75, 55, "Green"));
	        fruits.add(new Fruit("Mango", 115, 70, "Green"));
	        fruits.add(new Fruit("Watermelon", 101, 78, "Red"));

	        

	        // Grouping fruits by color
	        Map<String, List<String>> colorWiseFruits = new HashMap<>();
	        for (Fruit fruit : fruits) {
	            colorWiseFruits.computeIfAbsent(fruit.getColor(), k -> new ArrayList<>())
	                    .add(fruit.getName());
	        }

	        // Displaying color-wise list of fruit names
	        System.out.println("Color-wise list of fruit names:");
	        for (Map.Entry<String, List<String>> entry : colorWiseFruits.entrySet()) {
	            System.out.println(entry.getKey() + ": " + entry.getValue());

	}

}
}
