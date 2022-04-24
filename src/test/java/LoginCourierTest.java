import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    CourierClient courierClient = new CourierClient();

    @Test
    @DisplayName("курьер может авторизоваться")
    public void canLoginCourier() {
        Courier courier = new Courier("HermioneTheSmartest", "Avada Kedavra");

        courierClient.createCourier(courier);
        courierClient.login(courier).then().assertThat().statusCode(200);
        courierClient.deleteCourier(courier);
    }

    @Test
    @DisplayName("для авторизации нужно передать пароль")
    public void needPasswordToLoginCourier() {
        Courier courierWithoutPassword = new Courier("Vlad");

        courierClient.login(courierWithoutPassword).then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("для авторизации нужно передать логин")
    public void needLoginToLoginCourier() {
        Courier courierWithoutLogin = new Courier(null, "123");

        courierClient.login(courierWithoutLogin).then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("успешный запрос возвращает id")
    public void successLoginReturnsId() {
        Courier courier = new Courier("HermioneTheSmartest", "Avada Kedavra");

        courierClient.createCourier(courier);
        courierClient.login(courier).then().assertThat().body("id", notNullValue());
        courierClient.deleteCourier(courier);
    }
}
