package services;

import config.ApiConfig;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientService {
    private static final String BASE_PATH = ApiConfig.BASE_URL + "/ingredients";

    public String getIngridientName(String name) throws Exception {
        Response response =  given()
                .contentType("application/json")
                .when()
                .get(BASE_PATH);
        List<String> names = response.jsonPath().getList("data.name");

        int index = names.indexOf(name);

        if (index != -1) {
            return response.path("data[" + index + "]._id");
        } else {
            throw new Exception("Ингридиент не найден");
        }
    }
}
