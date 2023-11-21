package streamsAsmt;
import java.util.*;
import java.util.stream.Collectors;


public class StreamFourthQ {

	public static void main(String[] args) {
		
		List<News> newsList = new ArrayList<>();
        newsList.add(new News(1, "Josh", "Sam", "Great news!"));
        newsList.add(new News(2, "Sam", "Josh", "Interesting article."));
        newsList.add(new News(3, "Josh", "Sam", "Nice story."));
        newsList.add(new News(1, "Ron", "Josh", "Wow!!"));
        newsList.add(new News(1, "Josh", "Ron", "Good job!!"));
        newsList.add(new News(2, "Sam", "Ron", "Awesome!"));

        Map<Integer, Long> commentCountByNewsId = newsList.stream()
                .collect(Collectors.groupingBy(News::getNewsId, Collectors.counting()));

        Optional<Map.Entry<Integer, Long>> maxEntry = commentCountByNewsId.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent()) {
            System.out.println("News ID with maximum comments: " + maxEntry.get().getKey());
        } else {
            System.out.println("No news found.");
        }
		

	}

}

class News {
    int newsId;
    String postedByUser;
    String commentByUser;
    String comment;

    public News(int newsId, String postedByUser, String commentByUser, String comment) {
        this.newsId = newsId;
        this.postedByUser = postedByUser;
        this.commentByUser = commentByUser;
        this.comment = comment;
    }

    public int getNewsId() {
        return newsId;
    }
    
    public String getPostedByUser() {
    	return postedByUser;
    }
    
    public String getCommentByUser() {
    	return commentByUser;
    }
    
    public String getComment() {
    	return comment;
    }
}
