package by.sapra.restclientservice.web.controller.v2;

import by.sapra.restclientservice.mapper.v2.OrderMapperV2;
import by.sapra.restclientservice.service.OrderService;
import by.sapra.restclientservice.web.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/order")
@RequiredArgsConstructor
@Tag(name = "Order V2", description = "Order API version V2")
public class OrderControllerV2 {
    private final OrderService databaseOrderService;
    private final OrderMapperV2 mapper;


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
                mapper.orderListToOrderListResponse(databaseOrderService.findAll())
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
                mapper.orderToResponse(databaseOrderService.findById(orderId))
        );
    }
    @Operation(
            summary = "Get order by filter of parameters.",
            description = "Get order by filter. Return list of id, product and cost.",
            tags = {"order", "filter"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = OrderListResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/filter")
    public ResponseEntity<OrderListResponse> filterBy(@Valid OrderFilter filter) {
        return ResponseEntity.ok(
                mapper.orderListToOrderListResponse(databaseOrderService.filterBy(filter))
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
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.orderToResponse(databaseOrderService.save(mapper.requestToOrder(request)))
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
                mapper.orderToResponse(databaseOrderService.update(mapper.requestToOrder(orderId, request)))
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
        databaseOrderService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
