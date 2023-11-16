package by.sapra.restclientservice.service;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.reposytory.DatabaseOrderRepository;
import by.sapra.restclientservice.reposytory.OrderSpecification;
import by.sapra.restclientservice.utils.BeenUtils;
import by.sapra.restclientservice.web.model.OrderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseOrderService implements OrderService {
    private final DatabaseOrderRepository repository;

    private final DatabaseClintService databaseClintService;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Order> filterBy(OrderFilter filter) {
        return repository.findAll(OrderSpecification.withFilter(filter),
            PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format("Заказ с ID {0} не найден!", id))
        );
    }

    @Override
    public Order save(Order order) {
        Client client = databaseClintService.findById(order.getClient().getId());
        order.setClient(client);
        return repository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        Client client = databaseClintService.findById(order.getClient().getId());
        Order existedOrder = findById(order.getId());

        BeenUtils.copyNonNullProperties(order, existedOrder);
        existedOrder.setClient(client);

        return repository.save(existedOrder);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        repository.deleteAllById(ids);
    }
}
