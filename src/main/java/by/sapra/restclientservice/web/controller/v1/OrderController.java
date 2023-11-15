package by.sapra.restclientservice.web.controller.v1;

import by.sapra.restclientservice.mapper.v1.OrderMapper;
import by.sapra.restclientservice.service.OrderService;
import by.sapra.restclientservice.web.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Order V1", description = "Order API version V1")
public class OrderController {
    private final OrderService service;
    private final OrderMapper mapper;

    @Operation(
            summary = "Get all orders.",
            description = "Get all orders. Return list of id, product and cost.",
            tags = {"order"}
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = OrderListResponse.class))
    )
    @GetMapping
    public ResponseEntity<OrderListResponse> findAll() {
        return ResponseEntity.ok(
                mapper.orderListToOrderListResponse(service.findAll())
        );
    }

    @Operation(
            summary = "Get order by ID.",
            description = "Get order by ID. Return id, product and cost.",
            tags = {"order", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(
                mapper.orderToResponse(service.findById(orderId))
        );
    }

    @Operation(
            summary = "Save new order.",
            description = "Save new order. Return id, product and cost.",
            tags = {"order"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Not correct order request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody UpsertOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.orderToResponse(service.save(mapper.requestToOrder(request)))
        );
    }

    @Operation(
            summary = "Update order by ID.",
            description = "Update order by ID. Return id, product and cost.",
            tags = {"order", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = OrderResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
            responseCode = "400",
            description = "Not correct order request",
            content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }
    )
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") Long orderId, @RequestBody UpsertOrderRequest request) {
        return ResponseEntity.ok(
                mapper.orderToResponse(service.update(mapper.requestToOrder(orderId, request)))
        );
    }

    @Operation(
            summary = "Delete order by ID",
            description = "Delete order by Id. Return no content",
            tags = {"order", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(
                            implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
