package by.sapra.restclientservice.web.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponse {
    private Long id;
    private String product;
    private BigDecimal cost;
}
