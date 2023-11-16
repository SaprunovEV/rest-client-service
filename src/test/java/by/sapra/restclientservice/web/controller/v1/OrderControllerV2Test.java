package by.sapra.restclientservice.web.controller.v1;

import by.sapra.restclientservice.exception.EntityNotFoundException;
import by.sapra.restclientservice.mapper.v1.OrderMapper;
import by.sapra.restclientservice.model.Order;
import by.sapra.restclientservice.service.OrderService;
import by.sapra.restclientservice.testUtils.StringTestUtils;
import by.sapra.restclientservice.web.AbstractTestController;
import by.sapra.restclientservice.web.model.OrderListResponse;
import by.sapra.restclientservice.web.model.OrderResponse;
import by.sapra.restclientservice.web.model.UpsertOrderRequest;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {OrderController.class})
class OrderControllerV2Test extends AbstractTestController {
    @MockBean
    OrderService service;
    @MockBean
    OrderMapper mapper;

    private final String url = "/api/v1/order";

    @Test
    void whenFindAll_thenReturnAllOrders() throws Exception {
        List<Order> orders = List.of(
                createOrder(1L, 100L, null),
                createOrder(2L, 200L, null),
                createOrder(3L, 300L, null)
        );

        when(service.findAll()).thenReturn(orders);

        List<OrderResponse> orderResponses = orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            response.setId(order.getId());
            response.setCost(order.getCost());
            response.setProduct(order.getProduct());
            return response;
        }).toList();

        OrderListResponse orderListResponse = new OrderListResponse();
        orderListResponse.setOrders(orderResponses);

        when(mapper.orderListToOrderListResponse(orders)).thenReturn(orderListResponse);

        String actual = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        String expected = StringTestUtils.readStringFromResource("/response/orders/find_all_orders_response.json");

        JsonAssert.assertJsonEquals(expected, actual);

        verify(service, times(1)).findAll();
        verify(mapper, times(1)).orderListToOrderListResponse(orders);
    }

    @Test
    void whenGetOrderById_thenReturnOrderById() throws Exception {
        long id = 1L;

        Order order = createOrder(id, 100L, null);
        when(service.findById(id)).thenReturn(order);

        when(mapper.orderToResponse(order)).thenReturn(createOrderResponse(id, BigDecimal.valueOf(100)));

        String actual = mockMvc.perform(get(url + "/{id}", id))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = StringTestUtils.readStringFromResource("/response/orders/find_order_by_id_response.json");

        JsonAssert.assertJsonEquals(expected, actual);

        verify(service, times(1)).findById(id);
        verify(mapper, times(1)).orderToResponse(order);
    }

    @Test
    void whenGetNotExistClient_thenReturnError() throws Exception {
        long id = 500L;

        when(service.findById(id)).thenThrow(new EntityNotFoundException(
                MessageFormat.format("Заказ с ID {0} не найден!", id)
        ));

        MockHttpServletResponse response = mockMvc.perform(get(url + "/{id}", id))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        String actual = response.getContentAsString();

        String expected = StringTestUtils.readStringFromResource("/response/orders/order_by_id_not_found_response.json");

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("invalidProduct")
    void whenCreateOrderWithNotValidProduct_thenReturnError(String product) throws Exception {
        UpsertOrderRequest request = new UpsertOrderRequest();
        request.setProduct(product);
        request.setCost(BigDecimal.valueOf(100));

        MockHttpServletResponse response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");

        String actual = response.getContentAsString();

        String expected = StringTestUtils.readStringFromResource("/response/orders/product_not_valid_response.json");

        JsonAssert.assertJsonEquals(expected, actual);
    }

    public static Stream<Arguments> invalidProduct() {
        return Stream.of(
                Arguments.arguments(""),
                Arguments.arguments((Object) null)
        );
    }


}