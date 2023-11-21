package lambdaExpAsmt;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;



public class LambdaThirdQ {
    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, 13000, "ACCEPTED"));
        orders.add(new Order(2, 7000, "COMPLETED"));
        orders.add(new Order(3, 12000, "PENDING"));
        orders.add(new Order(4, 19000, "COMPLETED"));

        // Supplier - supplies the orders list
        Supplier<List<Order>> orderSupplier = () -> orders;

        // Consumer - prints order details
        Consumer<Order> printOrderDetails = order ->
                System.out.println("Order ID: " + order.getOrderId() + ", Price: " + order.getPrice() + ", Status: " + order.getStatus());

        // Predicate - checks criteria for filtering orders
        Predicate<Order> orderPredicate = order ->
                order.getPrice() > 10000 && (order.getStatus().equals("ACCEPTED") || order.getStatus().equals("COMPLETED"));

        // Function - filters and returns the filtered orders
        Function<List<Order>, List<Order>> filterOrders = orderList ->
                orderList.stream().filter(orderPredicate).collect(Collectors.toList());

        // Invoking the functional interfaces
        List<Order> filteredOrders = filterOrders.apply(orderSupplier.get());
        filteredOrders.forEach(printOrderDetails);
    }
}



