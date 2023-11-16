package by.sapra.restclientservice.reposytory;

import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.web.model.OrderFilter;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderSpecification {
    static Specification<Order> withFilter(OrderFilter filter) {
        return Specification.where(byProductName(filter.getProductName()))
                .and(byCostRange(filter.getMinCost(), filter.getMaxCost()))
                .and(byClientId(filter.getClientId()))
                .and(byCreateAtBefore(filter.getCreatedBefore()))
                .and(byUpdateAtBefore(filter.getUpdatedBefore()));
    }

    static Specification<Order> byUpdateAtBefore(Instant updatedBefore) {
        return ((root, query, criteriaBuilder) -> {
            if (updatedBefore == null) return null;
            return criteriaBuilder.lessThanOrEqualTo(root.get("updateAt"), updatedBefore);
        });
    }

    static Specification<Order> byCreateAtBefore(Instant createdBefore) {
        return ((root, query, criteriaBuilder) -> {
            if (createdBefore == null) return null;
            return criteriaBuilder.lessThanOrEqualTo(root.get("createAt"), createdBefore);
        });
    }

    static Specification<Order> byClientId(Long clientId) {
        return ((root, query, criteriaBuilder) -> {
            if (clientId==null) return null;
            return criteriaBuilder.equal(root.get("client").get("id"), clientId);
        });
    }

    static Specification<Order> byCostRange(BigDecimal minCost, BigDecimal maxCost) {
        return ((root, query, criteriaBuilder) -> {
            if (minCost == null && maxCost == null) return null;
            if (minCost == null) return criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxCost);
            if (maxCost == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost);
            return criteriaBuilder.between(root.get("cost"), minCost, maxCost);
        });
    }

    static Specification<Order> byProductName(String productName) {
        return (root, qe, cb) -> {
            if (productName == null) return null;

            return cb.equal(root.get("product"), productName);
        };
    }


}
