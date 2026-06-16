import helpers.BaseUrl;
import helpers.CreateOrderHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.CreateOrder;

import java.util.List;
import java.util.stream.Stream;

import static helpers.CreateOrderHelper.cancelOrder;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseUrl {
    private Integer createdOrder = null;

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
    @DisplayName("Проверка, что при создании заказа можно передавать разные цвета")
    public void createOrderWithColors(CreateOrder order) {

        Response response = new CreateOrderHelper().createOrder(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
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
