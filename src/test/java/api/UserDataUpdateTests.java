package api;

import dto.UserDTO;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.UserService;

import static org.junit.Assert.assertEquals;

public class UserDataUpdateTests {
    private final UserService userService = new UserService();
    private UserDTO user;
    private String token;

    @Before
    public void setUp() {
        String login = UserService.generateRandomString(8) + "@example.com";
        String password = UserService.generateRandomString(10);
        String name = UserService.generateRandomString(7);
        user = new UserDTO(login, password, name);
        userService.registerUser(user);
        Response response = userService.loginUser(user);
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
    @DisplayName("Успешное обновление данных пользователя")
    public void shouldUpdateUserDataSuccessfully() {
        UserDTO user = new UserDTO("updated.user@example.com", "password123", "UpdatedUser");
        Response response = userService.updateUser(token, user);

        assertEquals(200, response.getStatusCode());
        assertEquals("updated.user@example.com", response.jsonPath().getString("user.email"));
        assertEquals("UpdatedUser", response.jsonPath().getString("user.name"));
    }
}
