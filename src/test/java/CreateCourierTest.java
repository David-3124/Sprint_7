import helpers.BaseUrl;
import helpers.LoginCourierHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.CreateCourier;

import java.util.Random;

import static helpers.CreateCourierHelper.createCourierRequest;
import static helpers.DeleteCourierHelper.deleteCourier;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest extends BaseUrl {

    private String login;
    private String password;
    private String firstName;

    @BeforeEach
    @Step("Подготовка рандомных тестовых данных")
    public void generateTestData() {
        int random = new Random().nextInt(100);
        login = "Login_" + random;
        password = "Pass_" + random;
        firstName = "Name_" + random;
    }

    @Test
    @DisplayName("Создание курьера и проверка успешного запроса")
    public void creatingCourier() {

        CreateCourier courier = new CreateCourier(login, password, firstName);
        Response response = createCourierRequest(courier);
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров и проверка ошибки 409")
    public void creatingIdenticalCouriers() {

        CreateCourier courierOne = new CreateCourier(login, password, firstName);
        Response responseOne = createCourierRequest(courierOne);
        responseOne.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CreateCourier courierTwo = new CreateCourier(login, password, firstName);
        Response responseTwo = createCourierRequest(courierTwo);
        responseTwo.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без обязательного параметра и проверка ошибки 400")
    public void insufficientData() {

        CreateCourier courier = new CreateCourier(null, password, firstName);
        Response response = createCourierRequest(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterEach
    @Step("Очистка данных после выполнения тест кейса")
    public void cleaningData() {
        LoginCourierHelper loginHelper = new LoginCourierHelper();
        Integer courierId = loginHelper.getCourierId(login, password);
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }
}
