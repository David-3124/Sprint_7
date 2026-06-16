import helpers.BaseUrl;
import helpers.LoginCourierHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.CourierLogin;

import java.util.Random;

import static helpers.CreateCourierHelper.createNewCourier;
import static helpers.DeleteCourierHelper.deleteCourier;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest extends BaseUrl {

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
        createNewCourier(login, password, firstName);
    }

    @Test
    @DisplayName("Авторизация курьера")
    public void authorizationCourier() {

        CourierLogin courierLogin = new CourierLogin(login, password);
        Response response = new LoginCourierHelper().loginRequest(courierLogin);
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера без логина и проверка ошибки 400")
    public void insufficientData() {

        CourierLogin courierLogin = new CourierLogin(null, password);
        Response response = new LoginCourierHelper().loginRequest(courierLogin);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с неверными данными")
    public void nonExistentDate() {

        CourierLogin courierLogin = new CourierLogin(login, password + "123");
        Response response = new LoginCourierHelper().loginRequest(courierLogin);
        response.then()
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
