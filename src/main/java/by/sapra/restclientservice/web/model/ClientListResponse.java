package by.sapra.restclientservice.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClientListResponse {
    private List<ClintResponse> clients = new ArrayList<>();
}
