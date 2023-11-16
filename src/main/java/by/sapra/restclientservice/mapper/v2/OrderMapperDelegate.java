package by.sapra.restclientservice.mapper.v2;

import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.service.ClintService;
import by.sapra.restclientservice.web.model.UpsertOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class OrderMapperDelegate implements OrderMapperV2 {
    @Autowired
    private ClintService databaseClintService;


    @Override
    public Order requestToOrder(UpsertOrderRequest request) {
        return Order.builder()
                .cost(request.getCost())
                .product(request.getProduct())
                .client(databaseClintService.findById(request.getClientId()))
                .build();
    }

    @Override
    public Order requestToOrder(Long orderId, UpsertOrderRequest request) {
        Order order = requestToOrder(request);
        order.setId(orderId);
        return order;
    }
}
