package by.sapra.restclientservice.reposytory;

import by.sapra.restclientservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseClientRepository extends JpaRepository<Client, Long> {
}
