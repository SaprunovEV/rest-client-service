package by.sapra.restclientservice.service;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.reposytory.DatabaseClientRepository;
import by.sapra.restclientservice.utils.BeenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatabaseClintService implements ClintService {
    private final DatabaseClientRepository repository;

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Клиент с ID {0} не найден!", id)));
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Client client) {
        Client updatedClient = findById(client.getId());

        BeenUtils.copyNonNullProperties(client, updatedClient);

        return repository.save(updatedClient);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
