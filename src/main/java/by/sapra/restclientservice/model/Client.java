package by.sapra.restclientservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Client {
    private Long id;
    private String name;
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Long orderId) {
        orders.stream().filter(o -> !o.getId().equals(orderId)).toList();
    }
}
