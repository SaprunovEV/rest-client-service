package by.sapra.restclientservice.mapper.v1;

import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.web.model.ClientListResponse;
import by.sapra.restclientservice.web.model.ClintResponse;
import by.sapra.restclientservice.web.model.UpsertClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    private final OrderMapper orderMapper;

    public Client requestToClient(UpsertClientRequest request) {
        Client client = new Client();

        client.setName(request.getName());

        return client;
    }

    public Client requestToClient(Long clientId, UpsertClientRequest request) {
        Client client = requestToClient(request);
        client.setId(clientId);
        return client;
    }

    public ClintResponse clientToResponse(Client client) {
        ClintResponse response = new ClintResponse();

        response.setId(client.getId());
        response.setName(client.getName());
        response.setOrders(orderMapper.orderListToResponseList(client.getOrders()));

        return response;
    }

    public ClientListResponse clientListToClientListResponse(List<Client> clients) {
        List<ClintResponse> clintResponses = clients.stream().map(this::clientToResponse).toList();
        ClientListResponse clientListResponse = new ClientListResponse();
        clientListResponse.setClients(clintResponses);
        return clientListResponse;
    }
}
