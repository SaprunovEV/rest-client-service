package by.sapra.restclientservice.service;

import by.sapra.restclientservice.model.Client;

import java.util.List;

public interface ClintService {
    List<Client> findAll();

    Client findById(Long id);

    Client save(Client client);

    Client update(Client client);

    void deleteById(Long id);
}
