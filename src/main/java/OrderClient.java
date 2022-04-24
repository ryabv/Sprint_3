import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public OrderClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Step("Create order")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post();
    }

    @Step("Get orders list")
    public Response getOrdersList() {
        return given().get();
    }
}
