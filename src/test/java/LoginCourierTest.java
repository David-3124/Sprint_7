import helpers.ConfigHelper;
import helpers.CreateCourierHelper;
import helpers.LoginCourierHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CourierLogin;

import java.util.Random;

import static helpers.DeleteCourierHelper.deleteCourier;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

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
        new CreateCourierHelper().createNewCourier(login, password, firstName);
    }

    @Test
    @Step("Авторизация курьера")
    public void authorizationCourier() {
        CourierLogin courierLogin = new CourierLogin(login, password);
        given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post(ConfigHelper.COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Step("Авторизация курьера без логина и проверка ошибки 400")
    public void insufficientData() {
        CourierLogin courierLogin = new CourierLogin(null, password);
        given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post(ConfigHelper.COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Авторизация курьера с неверными данными")
    public void nonExistentDate() {
        CourierLogin courierLogin = new CourierLogin(login, password + "123");
        given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post(ConfigHelper.COURIER_LOGIN_ENDPOINT)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterEach
    @Step("Получение ID курьера и его удаление")
    public void cleaningData() {
        LoginCourierHelper loginHelper = new LoginCourierHelper();
        Integer courierId = loginHelper.getCourierId(login, password);
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }
}
