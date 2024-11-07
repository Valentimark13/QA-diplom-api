package api;

import dto.OrderCreationDTO;
import dto.UserDTO;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.IngredientService;
import services.OrderService;
import services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void shouldCreateOrderWithIngredients() throws Exception {
        OrderCreationDTO order = this.getDefaultBurger();
        Response response = orderService.createOrder(token, order);
        response.then().statusCode(200).body("success", equalTo(true));
    }

    @Step("Генерация данных для бургера")
    private OrderCreationDTO getDefaultBurger() throws Exception {
        IngredientService service = new IngredientService();
        List<String> ingridients = new ArrayList<>();
        ingridients.add(service.getIngridientName("Флюоресцентная булка R2-D3"));
        ingridients.add(service.getIngridientName("Говяжий метеорит (отбивная)"));

        return new OrderCreationDTO(ingridients);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void shouldReturnErrorForUnauthorizedUser() throws Exception {
        OrderCreationDTO order = this.getDefaultBurger();
        Response response = orderService.createOrder("", order);
        response.then().statusCode(401).body("message", equalTo("You should be authorised"));
    }
}
