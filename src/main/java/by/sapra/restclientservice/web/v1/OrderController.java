package by.sapra.restclientservice.web.v1;

import by.sapra.restclientservice.mapper.v1.OrderMapper;
import by.sapra.restclientservice.service.OrderService;
import by.sapra.restclientservice.web.model.OrderListResponse;
import by.sapra.restclientservice.web.model.OrderResponse;
import by.sapra.restclientservice.web.model.UpsertOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    private final OrderMapper mapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll() {
        return ResponseEntity.ok(
                mapper.orderListToOrderListResponse(service.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(
                mapper.orderToResponse(service.findById(orderId))
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody UpsertOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.orderToResponse(service.save(mapper.requestToOrder(request)))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") Long orderId, @RequestBody UpsertOrderRequest request) {
        return ResponseEntity.ok(
                mapper.orderToResponse(service.update(mapper.requestToOrder(orderId, request)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
