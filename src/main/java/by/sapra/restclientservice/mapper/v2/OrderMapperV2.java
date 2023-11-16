package by.sapra.restclientservice.mapper.v2;

import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.web.model.OrderListResponse;
import by.sapra.restclientservice.web.model.OrderResponse;
import by.sapra.restclientservice.web.model.UpsertOrderRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@DecoratedWith(OrderMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface OrderMapperV2 {
    Order requestToOrder(UpsertOrderRequest request);

    @Mapping(source = "orderId", target = "id")
    Order requestToOrder(Long orderId, UpsertOrderRequest request);

    OrderResponse orderToResponse(Order order);

    List<OrderResponse> orderListToResponseList(List<Order> orders);

    default OrderListResponse orderListToOrderListResponse(List<Order> orders) {
        return new OrderListResponse(orderListToResponseList(orders));
    }
}
