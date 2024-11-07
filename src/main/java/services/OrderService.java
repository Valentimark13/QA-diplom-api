package services;

import config.ApiConfig;
import dto.OrderCreationDTO;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderService {
    private static final String BASE_PATH = ApiConfig.BASE_URL + "/orders";

    public Response createOrder(String token, OrderCreationDTO order) {
        return given()
                .header("Authorization", token)
                .contentType("application/json")
                .body(order)
                .when()
                .post(BASE_PATH);
    }

    public Response getOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_PATH);
    }
}