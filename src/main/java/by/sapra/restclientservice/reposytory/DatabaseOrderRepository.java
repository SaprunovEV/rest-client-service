package by.sapra.restclientservice.reposytory;

import by.sapra.restclientservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseOrderRepository extends JpaRepository<Order, Long> {
}
