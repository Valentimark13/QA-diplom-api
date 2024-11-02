package api;

import dto.OrderCreationDTO;
import dto.UserDTO;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.OrderService;
import services.UserService;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class OrderCreationTests {
    private OrderService orderService;
    private UserService userService;
    private String token;

    @Before
    public void setUp() {
        orderService = new OrderService();
        userService = new UserService();
        String login = UserService.generateRandomString(8) + "@example.com";
        String password = UserService.generateRandomString(10);
        String name = UserService.generateRandomString(7);
        userService.registerUser(new UserDTO(login, password, name));
        Response response = userService.loginUser(new UserDTO(login, password, name));
        token = response.path("accessToken");
    }

    @After
    @Step("Удаление пользователя после теста")
    public void tearDown() {
        if (token != null) {
            userService.deleteUser(token);
            token = null;  // Обнуляем токен после удаления
        }
    }

    @Test
    @Step("Создание заказа с авторизацией и ингредиентами")
    public void shouldCreateOrderWithIngredients() {
        OrderCreationDTO order = new OrderCreationDTO(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"));
        Response response = orderService.createOrder(token, order);
        response.then().statusCode(200).body("success", equalTo(true));
    }

    @Test
    @Step("Создание заказа без авторизации")
    public void shouldReturnErrorForUnauthorizedUser() {
        OrderCreationDTO order = new OrderCreationDTO(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"));
        Response response = orderService.createOrder("", order);
        response.then().statusCode(401).body("message", equalTo("You should be authorised"));
    }
}
