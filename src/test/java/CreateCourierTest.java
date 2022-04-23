import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
    }

    @Test
    @DisplayName("курьера можно создать со статусом 201 и ok: true")
    public void canCreateCourier() {
        Courier courier = new Courier("HermioneTheSmartest", "Avada Kedavra", "Hermione");

        try {
            createCourier(courier)
                    .then().assertThat().statusCode(201)
                    .and().assertThat().body("ok", equalTo(true));
        } finally {
            deleteCourier(courier);
        }
    }

    @Test
    @DisplayName("нельзя создать двух одинаковых курьеров")
    public void cannotCreateSimilarCouriers() {
        Courier courier = new Courier("Kolya", "abcd");

        try {
            createCourier(courier).then().assertThat().statusCode(201);
            createCourier(courier).then().assertThat().statusCode(409);
        } finally {
            deleteCourier(courier);
        }
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void needAllRequiredFieldsToCreateCourier() {
        Courier courierWithoutLogin = new Courier();
        Courier courierWithoutPassword = new Courier("Sergey");
        Courier courierWIthAllRequiredFields = new Courier("Sergey", "lalala");

        try {
            createCourier(courierWithoutLogin).then().assertThat().statusCode(400);
            createCourier(courierWithoutPassword).then().assertThat().statusCode(400);
            createCourier(courierWIthAllRequiredFields).then().assertThat().statusCode(201);
        } finally {
            deleteCourier(courierWIthAllRequiredFields);
        }
    }

    @Test
    @DisplayName("если одного из полей нет, запрос возвращает ошибку")
    public void throwErrorIfMissingRequiredFields() {
        Courier courierWithoutLogin = new Courier();
        Courier courierWithoutPassword = new Courier("Petr");

        createCourier(courierWithoutLogin)
                .then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        createCourier(courierWithoutPassword)
                .then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void throwErrorIfCreatingCourierWithExistingLogin() {
        Courier courier = new Courier("Ivanov", "123");

        try {
            createCourier(courier).then().statusCode(201);
            createCourier(courier)
                    .then().assertThat()
                    .body("message", equalTo("Этот логин уже используется"));
        } finally {
            deleteCourier(courier);
        }
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
