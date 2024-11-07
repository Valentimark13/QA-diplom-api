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

public class UserLoginTests {

    private UserService userService;
    private String token;
    private UserDTO user;

    @Before
    public void setUp() {
        userService = new UserService();

        // Генерация случайных данных
        String login = UserService.generateRandomString(8) + "@example.com";
        String password = UserService.generateRandomString(10);
        String name = UserService.generateRandomString(7);

        user = new UserDTO(login, password, name);
        Response response = userService.registerUser(user);
        token = response.path("accessToken");
    }

    @After
    @Step("Удаление пользователя после теста")
    public void tearDown() {
        if (user != null) {
            userService.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    public void shouldLoginSuccessfully() {
        Response response = userService.loginUser(user);
        assertLoginSuccess(response);
    }

    @Test
    @DisplayName("Должна возвращаться ошибка при неверных авторизационных данных")
    public void shouldReturnErrorForInvalidCredentials() {
        UserDTO invalidUser = new UserDTO("wrong.email@example.com", "wrongPassword", "InvalidName");
        Response response = userService.loginUser(invalidUser);
        assertLoginFailure(response, "email or password are incorrect");
    }

    @Test
    @DisplayName("Должна возвращаться ошибка при пропущенных полях")
    public void shouldReturnErrorForMissingFields() {
        UserDTO incompleteUser = new UserDTO("", "", "");
        Response response = userService.loginUser(incompleteUser);
        assertLoginFailure(response, "email or password are incorrect");
    }

    @Step("Проверка успешного логина")
    private void assertLoginSuccess(Response response) {
        response.then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Step("Проверка ошибки при логине")
    private void assertLoginFailure(Response response, String expectedMessage) {
        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedMessage));
    }
}
