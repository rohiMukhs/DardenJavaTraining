package streamsAsmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamSeventhQ {

	public static void main(String[] args) {
		
		List<News> newsList = new ArrayList<>();
        newsList.add(new News(1, "Josh", "Sam", "Great news about budget!"));
        newsList.add(new News(2, "Sam", "Josh", "Interesting article."));
        newsList.add(new News(3, "Josh", "Sam", "Budget information should be included."));
        newsList.add(new News(1, "Ron", "Josh", "The budget information is not clear"));
        newsList.add(new News(1, "Josh", "Ron", "Good job!!"));
        newsList.add(new News(2, "Sam", "Ron", "Budgeting is crucial!"));
        newsList.add(new News(2, "Josh", "Sam", "Nice post!!"));
        
        Map<String, Long> commentsByUser = newsList.stream()
                .collect(Collectors.groupingBy(News::getCommentByUser, Collectors.counting()));

        System.out.println("CommentByUser-wise number of comments:");
        commentsByUser.forEach((user, count) ->
                System.out.println(user + ": " + count));

	}

}
