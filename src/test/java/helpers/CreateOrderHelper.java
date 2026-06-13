package helpers;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class CreateOrderHelper {

    @Step("Отмена заказа")
    public static void cancelOrder(int order) {
        given()
                .header("Content-type", "application/json")
                .body("{\"track\": " + order + "}")
                .when()
                .put(ConfigHelper.CANCEL_ORDER_ENDPOINT);
        System.out.println("Заказ отмене. track: " + order);
    }
}
