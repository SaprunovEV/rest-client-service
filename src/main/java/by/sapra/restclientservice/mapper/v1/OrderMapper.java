package by.sapra.restclientservice.mapper.v1;

import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.service.ClintService;
import by.sapra.restclientservice.web.model.OrderListResponse;
import by.sapra.restclientservice.web.model.OrderResponse;
import by.sapra.restclientservice.web.model.UpsertOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ClintService inMemoryClintService;

    public Order requestToOrder(UpsertOrderRequest request) {
        Order order = new Order();

        order.setCost(request.getCost());
        order.setProduct(request.getProduct());
        order.setClient(inMemoryClintService.findById(request.getClientId()));

        return order;
    }

    public Order requestToOrder(Long id, UpsertOrderRequest request) {
        Order order = requestToOrder(request);
        order.setId(id);
        return order;
    }

    public OrderResponse orderToResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setCost(order.getCost());
        orderResponse.setProduct(order.getProduct());

        return orderResponse;
    }

    public List<OrderResponse> orderListToResponseList(List<Order> orders) {
        return orders.stream().map(this::orderToResponse).toList();
    }

    public OrderListResponse orderListToOrderListResponse(List<Order> orders) {
        OrderListResponse orderListResponse = new OrderListResponse();
        orderListResponse.setOrders(orderListToResponseList(orders));
        return orderListResponse;
    }
}
