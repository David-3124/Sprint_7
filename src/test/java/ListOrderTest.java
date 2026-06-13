import helpers.ConfigHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrderTest {

    @BeforeEach
    @Step("Получение URL")
    public void loginUrl() {
        RestAssured.baseURI = ConfigHelper.PAGE_URL;
    }

    @Test
    @Step("Проверка, что в тело ответа возвращается список заказов")
    public void getListOrder() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(ConfigHelper.LIST_ORDERS_ENDPOINT)
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .extract().response();

        System.out.println(response.asPrettyString());
    }
}
