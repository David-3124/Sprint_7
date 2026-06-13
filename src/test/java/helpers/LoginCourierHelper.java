package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.CourierLogin;

import static io.restassured.RestAssured.given;

public class LoginCourierHelper {

    @Step("Получение id курьера")
    public Integer getCourierId(String login, String password) {

        CourierLogin loginData = new CourierLogin(login, password);

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginData)
                .when()
                .post(ConfigHelper.COURIER_LOGIN_ENDPOINT);

        if (loginResponse.statusCode() == 200) {
            return loginResponse.jsonPath().getInt("id");
        }
        return null;
    }
}
