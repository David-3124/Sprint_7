package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ListOrderHelper {

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ConfigHelper.LIST_ORDERS_ENDPOINT);
    }
}
