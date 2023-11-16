package by.sapra.restclientservice.reposytory.impl;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.reposytory.ClientRepository;
import by.sapra.restclientservice.reposytory.OrderRepository;
import by.sapra.restclientservice.utils.BeenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class InMemoryClientRepository implements ClientRepository {
    private OrderRepository orderRepository;

    private final Map<Long, Client> clients = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<Client> findAll() {
        return clients.values().stream().toList();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public Client save(Client client) {
        client.setId(nextId.incrementAndGet());
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public Client update(Client client) {
        Long clientId = client.getId();
        Client currentClient = clients.get(clientId);
        if (currentClient == null) {
            throw new EntityNotFoundException(MessageFormat.format("Клиента с id {0} не найдено", clientId));
        }
        BeenUtils.copyNonNullProperties(client, currentClient);
        currentClient.setId(clientId);

        clients.put(clientId, currentClient);

        return currentClient;
    }

    @Override
    public void deleteById(Long id) {
        Client currentClient = clients.get(id);

        if (currentClient == null) {
            throw new EntityNotFoundException(MessageFormat.format("Клиента с id {0} не найдено", id));
        }

        orderRepository.deleteByIdIn(currentClient.getOrders().stream().map(Order::getId).toList());
        clients.remove(id);
    }
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
