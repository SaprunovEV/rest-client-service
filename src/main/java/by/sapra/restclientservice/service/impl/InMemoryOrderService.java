package by.sapra.restclientservice.service.impl;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.reposytory.OrderRepository;
import by.sapra.restclientservice.service.OrderService;
import by.sapra.restclientservice.web.model.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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
    public List<Order> filterBy(OrderFilter filter) {
        throw new NotImplementedException();
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
}
