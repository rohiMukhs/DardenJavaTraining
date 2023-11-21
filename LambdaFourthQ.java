package lambdaExpAsmt;
import java.util.*;

public class LambdaFourthQ {
	
	public static void main(String args[]) {
		List<String> words = new ArrayList<>();
        words.add("hyundai");
        words.add("mercedez");
        words.add("bmw");
        words.add("audi");
        words.add("suzuki");
        words.add("honda");
        words.add("jaguar");

        System.out.println("List before removing any odd-length words: " + words);

        words.removeIf(word -> word.length() % 2 != 0);

        System.out.println("List after removing odd-length words: " + words);
	}

}
