package by.sapra.restclientservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientWithOrderRequest {
    private String name;
    private List<OrderRequest> orders;
}
