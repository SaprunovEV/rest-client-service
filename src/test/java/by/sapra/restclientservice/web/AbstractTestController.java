package by.sapra.restclientservice.web;

import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.web.model.ClintResponse;
import by.sapra.restclientservice.web.model.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

public abstract class AbstractTestController {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Client createClient(Long id, Order order) {
        Client client = new Client();
        client.setId(id);
        client.setName("Client " + id);
        client.setOrders(new ArrayList<>());

        if (order != null) client.getOrders().add(order);

        return client;
    }

    protected Order createOrder(Long id, Long cost, Client client) {
        Order order = new Order();

        order.setId(id);
        order.setProduct("Test product " + id);
        order.setCost(BigDecimal.valueOf(cost));
        order.setClient(client);
        order.setCreateAt(Instant.now());
        order.setUpdateAt(Instant.now());

        return order;
    }

    protected ClintResponse createClientResponse(Long id, OrderResponse orderResponse) {
        ClintResponse clintResponse = new ClintResponse();

        clintResponse.setId(id);
        clintResponse.setName("Client " + id);
        clintResponse.setOrders(new ArrayList<>());

        if (orderResponse != null) clintResponse.getOrders().add(orderResponse);

        return clintResponse;
    }

    protected OrderResponse createOrderResponse(Long id, BigDecimal cost) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(id);
        orderResponse.setCost(cost);
        orderResponse.setProduct("Test product " + id);

        return orderResponse;
    }
}
