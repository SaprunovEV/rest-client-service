package by.sapra.restclientservice.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpsertOrderRequest {
    private Long clientId;
    @NotBlank(message = "Товар не может быть пустым или null!")
    private String product;
    private BigDecimal cost;
}
