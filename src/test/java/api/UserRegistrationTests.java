package api;

import dto.UserDTO;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.UserService;
import static org.hamcrest.Matchers.equalTo;

public class UserRegistrationTests {
    private UserService userService;
    private String token;

    @Before
    public void setUp() {
        userService = new UserService();
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
    @DisplayName("Создание уникального пользователя")
    public void shouldCreateUniqueUser() {
        String login = UserService.generateRandomString(8) + "@example.com";
        String password = UserService.generateRandomString(10);
        String name = UserService.generateRandomString(7);
        Response response = userService.registerUser(new UserDTO(login, password, name));
        response.then().statusCode(200).body("success", equalTo(true));
        token = response.path("accessToken");
    }

    @Test
    @DisplayName("Попытка создать уже зарегистрированного пользователя")
    public void shouldReturnErrorForDuplicateUser() {
        String login = UserService.generateRandomString(8) + "@example.com";
        String password = UserService.generateRandomString(10);
        String name = UserService.generateRandomString(7);
        UserDTO user = new UserDTO(login, password, name);
        token = userService.registerUser(user).path("accessToken");  // Регистрируем пользователя впервые
        Response response = userService.registerUser(user);  // Пытаемся зарегистрировать повторно
        response.then().statusCode(403).body("message", equalTo("User already exists"));
    }
}
