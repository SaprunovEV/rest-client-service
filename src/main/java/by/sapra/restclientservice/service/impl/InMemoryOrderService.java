package by.sapra.restclientservice.service.impl;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.exception.UpdateStateException;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.reposytory.OrderRepository;
import by.sapra.restclientservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryOrderService implements OrderService {
    private final OrderRepository repository;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Ордер с ID {0} не найден", id)
        ));
    }

    @Override
    public Order save(Order order) {
        return repository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        return repository.update(order);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        repository.deleteByIdIn(ids);
    }

    private void checkForUpdate(Long id) {
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
