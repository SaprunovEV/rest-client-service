package by.sapra.restclientservice.service.impl;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.reposytory.ClientRepository;
import by.sapra.restclientservice.service.ClintService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryClintService implements ClintService {
    private final ClientRepository repository;

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Клиент с ID {0} не найден", id)
        ));
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Client client) {
        return repository.update(client);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Client saveWithOrders(Client client, List<Order> orders) {
        throw new NotImplementedException();
    }
}
