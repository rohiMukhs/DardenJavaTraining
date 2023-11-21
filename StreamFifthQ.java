package streamsAsmt;

import java.util.ArrayList;
import java.util.List;

public class StreamFifthQ {

	public static void main(String[] args) {
		
		List<News> newsList = new ArrayList<>();
        newsList.add(new News(1, "Josh", "Sam", "Great news about budget!"));
        newsList.add(new News(2, "Sam", "Josh", "Interesting article."));
        newsList.add(new News(3, "Josh", "Sam", "Budget information should be included."));
        newsList.add(new News(1, "Ron", "Josh", "The budget information is not clear"));
        newsList.add(new News(1, "Josh", "Ron", "Good job!!"));
        newsList.add(new News(2, "Sam", "Ron", "Budgeting is crucial!"));
        
        long budgetWordOccurrences = newsList.stream()
                .map(News::getComment)
                .filter(comment -> comment.toLowerCase().contains("budget"))
                .count();

        System.out.println("Occurrences of 'budget' in user comments across all news: "
        + budgetWordOccurrences);

	}

}
