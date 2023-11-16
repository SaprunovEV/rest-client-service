package by.sapra.restclientservice.web.model;

import by.sapra.restclientservice.validation.OrderFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@OrderFilterValid
public class OrderFilter {
    private String productName;
    private Integer pageSize;
    private Integer pageNumber;
    private BigDecimal minCost;
    private BigDecimal maxCost;
    private Instant createdBefore;
    private Instant updatedBefore;
    private Long clientId;
}
