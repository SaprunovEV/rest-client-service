package by.sapra.restclientservice.web.controller.v1;


import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.mapper.v1.ClientMapper;
import by.sapra.restclientservice.model.Client;
import by.sapra.restclientservice.service.ClintService;
import by.sapra.restclientservice.testUtils.StringTestUtils;
import by.sapra.restclientservice.web.AbstractTestController;
import by.sapra.restclientservice.web.model.ClientListResponse;
import by.sapra.restclientservice.web.model.ClintResponse;
import by.sapra.restclientservice.web.model.UpsertClientRequest;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest extends AbstractTestController {
    @MockBean
    ClintService service;
    @MockBean
    ClientMapper mapper;
    private final String url = "/api/v1/client";

    @Test
    void whenFindAll_thenReturnAllClients() throws Exception {
        ArrayList<Client> clients = new ArrayList<>();

        clients.add(createClient(1L, null));
        clients.add(createClient(2L, createOrder(1L, 100L, null)));

        List<ClintResponse> clintResponses = new ArrayList<>();

        clintResponses.add(createClientResponse(1L, null));
        clintResponses.add(createClientResponse(2L, createOrderResponse(1L, BigDecimal.valueOf(100L))));

        ClientListResponse listResponse = new ClientListResponse();
        listResponse.setClients(clintResponses);

        when(service.findAll()).thenReturn(clients);
        when(mapper.clientListToClientListResponse(clients)).thenReturn(listResponse);

        String actualResponse = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/clients/find_all_clients_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

        verify(service, times(1)).findAll();
        verify(mapper, times(1)).clientListToClientListResponse(clients);
    }

    @Test
    void whenGetClientById_thenReturnClientById() throws Exception {
        long id = 1L;
        Client client = createClient(id, null);
        ClintResponse clientResponse = createClientResponse(id, null);

        when(service.findById(id)).thenReturn(client);
        when(mapper.clientToResponse(client)).thenReturn(clientResponse);

        String actualRequest = mockMvc.perform(get(url + "/{id}", id))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/clients/find_client_by_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualRequest);

        verify(service, times(1)).findById(id);
        verify(mapper, times(1)).clientToResponse(client);
    }

    @Test
    void whenUpdateClient_thenReturnUpdatedClient() throws Exception {
        long id = 1L;
        String newName = "New client " + id;

        UpsertClientRequest request = new UpsertClientRequest();
        request.setName(newName);

        Client client = createClient(id, null);

        when(mapper.requestToClient(id, request)).thenReturn(client);

        Client updatedClient = createClient(id, null);
        updatedClient.setName(newName);

        when(service.update(client)).thenReturn(updatedClient);

        ClintResponse clientResponse = createClientResponse(id, null);
        clientResponse.setName(newName);

        when(mapper.clientToResponse(updatedClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(
                        put(url + "/{id}", id)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/clients/update_client_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

        verify(mapper, times(1)).requestToClient(id, request);
        verify(service, times(1)).update(client);
        verify(mapper, times(1)).clientToResponse(updatedClient);
    }

    @Test
    void whenCreateClient_thenReturnNewClient() throws Exception {
        long id = 1L;
        UpsertClientRequest request = new UpsertClientRequest();
        request.setName("Client " + id);

        Client client = createClient(id, null);
        when(mapper.requestToClient(request)).thenReturn(client);

        when(service.save(client)).thenReturn(client);

        ClintResponse clientResponse = createClientResponse(id, null);
        when(mapper.clientToResponse(client)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(
                        post(url)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/clients/create_client_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

        verify(mapper, times(1)).requestToClient(request);
        verify(service, times(1)).save(client);
        verify(mapper, times(1)).clientToResponse(client);
    }

    @Test
    void whenDeleteClient_thenCallDeleteFromService_andThenReturnNoContent() throws Exception {
        long id = 1L;

        String actualResponse = mockMvc.perform(delete(url + "/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn().getResponse()
                .getContentAsString();

        verify(service, times(1)).deleteById(id);
    }

    @Test
    void whenGetNotExistClient_thenReturnError() throws Exception {
        long id = 500L;

        when(service.findById(id)).thenThrow(new EntityNotFoundException(MessageFormat.format("Клиент с ID {0} не найден!", id)));

        MockHttpServletResponse response = mockMvc.perform(get(url + "/{id}", id))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/clients/client_by_id_not_found_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void whenCreateClientWithEmptyName_thenReturnError() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(new UpsertClientRequest())))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualContent = response.getContentAsString();

        String expectedContent = StringTestUtils.readStringFromResource("/response/clients/not_valid_name_response.json");

        JsonAssert.assertJsonEquals(expectedContent, actualContent);
    }

    @ParameterizedTest
    @MethodSource("invalidSizeName")
    public void whenNameSizeIsInvalid_returnError(String name) throws Exception {
        UpsertClientRequest re = new UpsertClientRequest();
        re.setName(name);

        MockHttpServletResponse response = mockMvc.perform(post(url).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(re)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualContent = response.getContentAsString();

        String expectedContent = StringTestUtils.readStringFromResource("/response/clients/client_name_size_exception_response.json");

        JsonAssert.assertJsonEquals(expectedContent, actualContent);
    }


    private static Stream<Arguments> invalidSizeName() {
        return Stream.of(
                Arguments.arguments(RandomString.make(2)),
                Arguments.arguments(RandomString.make(31))
        );
    }
}
