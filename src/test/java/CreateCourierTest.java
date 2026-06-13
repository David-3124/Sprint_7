import helpers.ConfigHelper;
import helpers.LoginCourierHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CreateCourier;

import java.util.Random;

import static helpers.DeleteCourierHelper.deleteCourier;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private String login;
    private String password;
    private String firstName;

    @BeforeEach
    @Step("Подготовка рандомных тестовых данных")
    public void loginUrl() {
        RestAssured.baseURI = ConfigHelper.PAGE_URL;
        int random = new Random().nextInt(100);
        login = "Login_" + random;
        password = "Pass_" + random;
        firstName = "Name_" + random;
    }

    @Test
    @Step("Создание курьера и проверка успешного запроса")
    public void creatingCourier() {

        CreateCourier courier = new CreateCourier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(ConfigHelper.CREATE_COURIER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @Step("Создание двух одинаковых курьеров и проверка ошибки 409")
    public void creatingIdenticalCouriers() {

        CreateCourier courierOne = new CreateCourier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(courierOne)
                .when()
                .post(ConfigHelper.CREATE_COURIER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CreateCourier courierTwo = new CreateCourier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(courierTwo)
                .when()
                .post(ConfigHelper.CREATE_COURIER_ENDPOINT)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @Step("Создание курьера без обязательного параметра и проверка ошибки 400")
    public void insufficientData() {

        CreateCourier courier = new CreateCourier(null, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(ConfigHelper.CREATE_COURIER_ENDPOINT)
                .then()
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
