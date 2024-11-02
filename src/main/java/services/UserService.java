package services;

import config.ApiConfig;
import dto.UserDTO;
import io.restassured.response.Response;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserService {
    private static final String BASE_PATH = ApiConfig.BASE_URL + "/auth";

    public Response registerUser(UserDTO user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(BASE_PATH + "/register");
    }

    public Response loginUser(UserDTO user) {
        return given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(BASE_PATH + "/login");
    }

    public Response deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete(BASE_PATH + "/user");
    }

    public Response updateUser(String token, UserDTO updatedUser) {
        return given()
                .header("Authorization", token)
                .contentType("application/json")
                .body(updatedUser)
                .when()
                .patch(BASE_PATH + "/user");
    }

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}