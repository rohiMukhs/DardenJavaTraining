package lambdaExpAsmt;
import java.util.ArrayList;
import java.util.List;

public class LambdaSecondQ {
	
	public static void main(String[] args) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, 13000, "ACCEPTED"));
        orders.add(new Order(2, 7000, "COMPLETED"));
        orders.add(new Order(3, 12000, "PENDING"));
        orders.add(new Order(4, 19000, "COMPLETED"));

        System.out.println("Orders that meet the required conditions:");
        orders.stream()
                .filter(order -> order.getPrice() > 10000 && (order.getStatus().equals("ACCEPTED") || order.getStatus().equals("COMPLETED")))
                .forEach(order -> System.out.println("Order ID: " + order.getOrderId() + ", Price: " + order.getPrice() + ", Status: " + order.getStatus()));
    }

}


class Order {
    private int orderId;
    private double price;
    private String status;

    public Order(int orderId, double price, String status) {
        this.orderId = orderId;
        this.price = price;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}



