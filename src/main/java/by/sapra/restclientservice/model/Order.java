package by.sapra.restclientservice.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Order {
    private Long id;
    private String product;
    private Instant createAt;
    private Instant updateAt;

    private Client client;
}
