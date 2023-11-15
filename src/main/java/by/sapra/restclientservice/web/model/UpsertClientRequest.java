package by.sapra.restclientservice.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertClientRequest {
    @NotBlank(message = "Имя клиента должно быть заполнено!")
    @Size(min = 3, max = 30, message = "Имя должно быть между {min} и {max} символами")
    private String name;
}
