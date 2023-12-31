package by.sapra.restclientservice.web.controller.v2;

import by.sapra.restclientservice.mapper.v2.ClientMapperV2;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.service.ClintService;
import by.sapra.restclientservice.web.model.ClientListResponse;
import by.sapra.restclientservice.web.model.ClintResponse;
import by.sapra.restclientservice.web.model.CreateClientWithOrderRequest;
import by.sapra.restclientservice.web.model.UpsertClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client")
@RequiredArgsConstructor
public class ClientControllerV2 {
    private final ClintService databaseClintService;
    private final ClientMapperV2 mapper;

    @GetMapping
    public ResponseEntity<ClientListResponse> findAll() {
        return ResponseEntity.ok(
                mapper.clientListToClientListResponse(databaseClintService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClintResponse> findById(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok(
                mapper.clientToResponse(databaseClintService.findById(clientId))
        );
    }

    @PostMapping
    public ResponseEntity<ClintResponse> createClient(@RequestBody UpsertClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.clientToResponse(
                        databaseClintService.save(
                                mapper.requestToClient(request)
                        )
                )
        );
    }

    @PostMapping("/save-with-orders")
    public ResponseEntity<ClintResponse> saveWithOrder(@RequestBody CreateClientWithOrderRequest request) {
        List<Order> orders = request.getOrders().stream()
                .map(orderRequest -> Order.builder()
                        .cost(orderRequest.getCost())
                        .product(orderRequest.getProduct())
                        .build()).toList();
        Client client = Client.builder().name(request.getName()).build();

        return ResponseEntity.ok(mapper.clientToResponse(databaseClintService.saveWithOrders(client, orders)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClintResponse> updateClient(@PathVariable("id") Long clientId, @RequestBody UpsertClientRequest request) {
        return ResponseEntity.ok(
                mapper.clientToResponse(
                        databaseClintService.update(
                                mapper.requestToClient(clientId, request)
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long clientId) {
        databaseClintService.deleteById(clientId);
        return ResponseEntity.noContent().build();
    }
}
