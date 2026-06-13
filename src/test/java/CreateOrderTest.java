import helpers.ConfigHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.CreateOrder;

import java.util.List;
import java.util.stream.Stream;

import static helpers.CreateOrderHelper.cancelOrder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    private Integer createdOrder = null;

    @BeforeEach
    @Step("Получение URL")
    public void loginUrl() {
        RestAssured.baseURI = ConfigHelper.PAGE_URL;
    }

    static Stream<CreateOrder> orderProvider() {
        return Stream.of(
                new CreateOrder("Петр", "Петров", "Moscow, 12 apt.", "4",
                        "+7 999 333 22 55", 5, "2026-06-13", "Оставить у двери",
                        List.of("BLACK")),
                new CreateOrder("Петр", "Петров", "Moscow, 12 apt.", "4",
                        "+7 999 333 22 55", 5, "2026-06-13", "Оставить у двери",
                        List.of("GREY")),
                new CreateOrder("Петр", "Петров", "Moscow, 12 apt.", "4",
                        "+7 999 333 22 55", 5, "2026-06-13", "Оставить у двери",
                        List.of("BLACK", "GREY")),
                new CreateOrder("Петр", "Петров", "Moscow, 12 apt.", "4",
                        "+7 999 333 22 55", 5, "2026-06-13", "Оставить у двери",
                        List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("orderProvider")
    @Step("Проверка, что при создании заказа можно передвать разные цвета")
    public void createOrderWithColors(CreateOrder order) {

        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ConfigHelper.CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response();

        createdOrder = response.jsonPath().getInt("track");
    }

    @AfterEach
    @Step("Отмена созданного заказа")
    public void cancellation() {
        if (createdOrder != null) {
            cancelOrder(createdOrder);
        }
    }
}
