import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Test
    public void returnsOrderList() {
        Order order = new Order(
                "firstName",
                "lastName",
                "address",
                "Center",
                "12931839",
                1000,
                "10.10.2022",
                "-",
                new String[]{"BLACK", "GREY"}
        );

        createOrder(order).then().assertThat().statusCode(201);
        getOrdersList().then().assertThat().body("orders", notNullValue());
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
