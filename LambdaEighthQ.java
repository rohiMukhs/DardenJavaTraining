package lambdaExpAsmt;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class LambdaEighthQ {

	public static void main(String[] args) {
		
		List<Integer> numbers = Arrays.asList(12, 22, 63, 90, 51);

        // Implementing the Consumer interface to print numbers
        Consumer<List<Integer>> printNumbers = numList -> {
            for (Integer num : numList) {
                System.out.println(num);
            }
        };

        // Creating a new thread to print numbers
        Thread numberPrintingThread = new Thread(() -> printNumbers.accept(numbers));

        // Starting the thread
        numberPrintingThread.start();

	}

}
