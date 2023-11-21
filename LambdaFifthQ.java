package lambdaExpAsmt;
import java.util.*;
import java.util.function.Consumer;

public class LambdaFifthQ {

	public static void main(String[] args) {
		
		 
		        List<String> words = new ArrayList<>();
		        words.add("hyundai");
		        words.add("mercedez");
		        words.add("bmw");
		        words.add("audi");
		        words.add("suzuki");
		        words.add("honda");
		        words.add("jaguar");

		        StringBuilder resultingString = new StringBuilder();

		        Consumer<String> appendFirstLetter = word -> {
		            if (!word.isEmpty()) {
		            	resultingString.append(word.charAt(0));
		            }
		        };

		        words.forEach(appendFirstLetter);

		        System.out.println("Resulting string: " + resultingString.toString());
		    }

	

}
