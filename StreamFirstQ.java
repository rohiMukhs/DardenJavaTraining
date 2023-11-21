package streamsAsmt;

import java.util.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamFirstQ {

	public static void main(String[] args) {

		 List<Fruit> fruits = new ArrayList<>();
	        fruits.add(new Fruit("Apple", 95, 80, "Red"));
	        fruits.add(new Fruit("Orange", 45, 60, "Orange"));
	        fruits.add(new Fruit("Banana", 105, 30, "Yellow"));
	        fruits.add(new Fruit("Grapes", 80, 100, "Purple"));
	        fruits.add(new Fruit("Mango", 115, 50, "Green"));

	        // Filtering low calorie fruits and sorting in descending order of calories
	        List<String> lowCalorieFruitNames = fruits.stream()
	                .filter(fruit -> fruit.getCalories() < 100)
	                .sorted(Comparator.comparingInt(Fruit::getCalories).reversed())
	                .map(Fruit::getName)
	                .collect(Collectors.toList());

	        // Displaying the names of low calorie fruits
	        System.out.println("Names of low calorie fruits in descending order of their respective calories:");
	        lowCalorieFruitNames.forEach(System.out::println);
	}

}

class Fruit {
    String name;
    int calories;
    int price;
    String color;

    public Fruit(String name, int calories, int price, String color) {
        this.name = name;
        this.calories = calories;
        this.price = price;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }
        
    public String getColor() {
    	return color;
    }
    
    public int getPrice() {
    	return price;
    }
}
