package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.CreateCourier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierHelper {

    @Step("Создание курьера с проверкой успеха")
    public static CreateCourier createNewCourier(String login, String password, String firstName) {
        CreateCourier courier = new CreateCourier(login, password, firstName);
        Response response = createCourierRequest(courier);
        response.then().statusCode(201).body("ok", equalTo(true));
        return courier;
    }

    @Step("Запроса на создание курьера")
    public static Response createCourierRequest(CreateCourier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(ConfigHelper.CREATE_COURIER_ENDPOINT);
    }
}
