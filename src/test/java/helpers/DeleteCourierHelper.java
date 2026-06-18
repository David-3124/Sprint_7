package helpers;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierHelper {

    @Step("Удаление курьера")
    public static void deleteCourier(int courierId) {

        given()
                .header("Content-type", "application/json")
                .when()
                .delete(ConfigHelper.DELETE_COURIER_ENDPOINT + "/" + courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
        System.out.println("Курьер удален. ID: " + courierId);
    }
}