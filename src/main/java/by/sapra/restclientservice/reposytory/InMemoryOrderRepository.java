package by.sapra.restclientservice.reposytory;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.utils.BeenUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@NoArgsConstructor
public class InMemoryOrderRepository implements OrderRepository {
    private ClientRepository clientRepository;

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public List<Order> findAll() {
        return orders.values().stream().toList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public Order save(Order order) {
        Long orderId = currentId.incrementAndGet();
        Long clientId = order.getClient().getId();

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Клиент с ID {0} не найден", clientId)));

        order.setId(orderId);
        Instant now = Instant.now();
        order.setCreateAt(now);
        order.setUpdateAt(now);
        order.setClient(client);

        orders.put(orderId, order);

        client.addOrder(order);
        clientRepository.update(client);

        return order;
    }

    @Override
    public Order update(Order order) {
        Long orderId = order.getId();
        Instant now = Instant.now();
        Order currentOrder = orders.get(orderId);

        if (currentOrder == null) {
            throw new EntityNotFoundException(MessageFormat.format("Клиента с id {0} не найдено", orderId));
        }

        BeenUtils.copyNonNullProperties(order, currentOrder);
        currentOrder.setId(orderId);
        currentOrder.setUpdateAt(now);

        orders.put(orderId, currentOrder);

        return order;
    }

    @Override
    public void deleteById(Long id) {
        Order currentOrder = orders.get(id);

        if (currentOrder == null) {
            throw new EntityNotFoundException(MessageFormat.format("Клиента с id {0} не найдено", id));
        }

        currentOrder.getClient().removeOrder(currentOrder.getId());

        orders.remove(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
