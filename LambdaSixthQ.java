package lambdaExpAsmt;

import java.util.*;
import java.util.function.UnaryOperator;

public class LambdaSixthQ {

	public static void main(String[] args) {
		
		List<String> words = new ArrayList<>();
		words.add("hyundai");
        words.add("mercedez");
        words.add("bmw");
        words.add("audi");
        words.add("suzuki");
        words.add("honda");
        words.add("jaguar");

        UnaryOperator<String> toUppercase = String::toUpperCase;

        words.replaceAll(toUppercase);

        System.out.println("List after replacing with uppercase words: " + words);

	}

}
