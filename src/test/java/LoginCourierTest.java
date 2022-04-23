import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }

    @Test
    @DisplayName("курьер может авторизоваться")
    public void canLoginCourier() {
        Courier courier = new Courier("HermioneTheSmartest", "Avada Kedavra");

        createCourier(courier);
        login(courier).then().assertThat().statusCode(200);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("для авторизации нужно передать пароль")
    public void needPasswordToLoginCourier() {
        Courier courierWithoutPassword = new Courier("Vlad");

        login(courierWithoutPassword).then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("для авторизации нужно передать логин")
    public void needLoginToLoginCourier() {
        Courier courierWithoutLogin = new Courier(null, "123");

        login(courierWithoutLogin).then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("успешный запрос возвращает id")
    public void successLoginReturnsId() {
        Courier courier = new Courier("HermioneTheSmartest", "Avada Kedavra");

        createCourier(courier);
        login(courier).then().assertThat().body("id", notNullValue());
        deleteCourier(courier);
    }

    @Step("Create courier")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post();
    }

    @Step("Login")
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/login");
    }

    @Step("Delete courier")
    public void deleteCourier(Courier courier) {
        Response res = login(courier);

        if (res.then().extract().statusCode() != 200) {
            return;
        }

        Integer courierId = res.then().extract().body().path("id");

        given().delete("/" + courierId).then().assertThat().statusCode(200);
    }
}
