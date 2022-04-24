import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    public CourierClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/courier";
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
