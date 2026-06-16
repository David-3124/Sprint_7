import helpers.BaseUrl;
import helpers.ListOrderHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.greaterThan;

public class ListOrderTest extends BaseUrl {

    @Test
    @DisplayName("Проверка, что в тело ответа возвращается список заказов")
    public void getListOrder() {

        Response response = new ListOrderHelper().getOrders();
        response.then()
                .statusCode(200)
                .body("orders.size()", greaterThan(0));
        System.out.println(response.asPrettyString());
    }
}
