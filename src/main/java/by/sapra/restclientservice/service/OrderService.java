package by.sapra.restclientservice.service;

import by.sapra.restclientservice.exception.UpdateStateException;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.web.model.OrderFilter;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public interface OrderService {
    List<Order> findAll();
    List<Order> filterBy(OrderFilter filter);

    Order findById(Long id);

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);

    default void checkForUpdate(Long id) {
        Order currentOrder = findById(id);
        Instant now = Instant.now();

        Duration duration = Duration.between(currentOrder.getCreateAt(), now);

        if (duration.getSeconds() > 5L) {
            throw new UpdateStateException(
                    MessageFormat.format("Заказ с ID {0} невозможно обновить! Прошло более 5 секунд", id)
            );
        }
    }
}
