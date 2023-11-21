package streamsAsmt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamThirdQ {

	public static void main(String[] args) {
		
		List<Fruit> fruits = new ArrayList<>();
        fruits.add(new Fruit("Apple", 95, 50, "Red"));
        fruits.add(new Fruit("Orange", 45, 40, "Orange"));
        fruits.add(new Fruit("Banana", 105, 30, "Yellow"));
        fruits.add(new Fruit("Strawberry", 60, 45, "Red"));
        fruits.add(new Fruit("Cherry", 80, 55, "Red"));
        fruits.add(new Fruit("Grapes", 80, 60, "Purple"));
        fruits.add(new Fruit("Pear", 75, 55, "Green"));
        fruits.add(new Fruit("Mango", 115, 70, "Green"));
        fruits.add(new Fruit("Watermelon", 101, 78, "Red"));
        
     // Filtering red-colored fruits and sorting by price in ascending order
        List<Fruit> redFruitsSortedByPrice = fruits.stream()
                .filter(fruit -> fruit.getColor().equalsIgnoreCase("Red"))
                .sorted(Comparator.comparingInt(Fruit::getPrice))
                .collect(Collectors.toList());

        // Displaying red-colored fruits sorted by price
        System.out.println("Red-colored fruits sorted by price (ascending):");
        redFruitsSortedByPrice.forEach(fruit ->
        System.out.println(fruit.getName() + " - Price: $" + fruit.getPrice()));
	}

}
