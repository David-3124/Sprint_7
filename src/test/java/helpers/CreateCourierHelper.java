package helpers;

import io.qameta.allure.Step;
import utils.CreateCourier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierHelper {

    @Step("Создание курьера")
    public CreateCourier createNewCourier(String login, String password, String firstName) {
        CreateCourier courier = new CreateCourier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(ConfigHelper.CREATE_COURIER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
        return courier;
    }
}
