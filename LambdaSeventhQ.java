package lambdaExpAsmt;
import java.util.*;

public class LambdaSeventhQ {

	public static void main(String[] args) {
		
		Map<String, Integer> map = new HashMap<>();
        map.put("hyundai", 1);
        map.put("bmw", 2);
        map.put("audi", 3);
        map.put("mercedez", 4);
        
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }

        // Removing the extra ", " at the end
        if (result.length() > 0) {
            result.setLength(result.length() - 2);
        }

        System.out.println("Resulting string: " + result.toString());

	}

}
