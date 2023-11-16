package by.sapra.restclientservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@ToString
@Entity(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String product;
    private BigDecimal cost;
    @CreationTimestamp
    private Instant createAt;
    @UpdateTimestamp
    private Instant updateAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;
}
