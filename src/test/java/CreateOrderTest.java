import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.basePath = "/api/v1/orders";
    }

    @Test
    @DisplayName("можно совсем не указывать цвет")
    public void canCreateOrderWithoutColors() {
        Order order = new Order(
                "firstName",
                "lastName",
                "address",
                "Center",
                "12931839",
                1000,
                "10.10.2022",
                "-",
                null
        );

        createOrder(order).then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("можно указать один из цветов — BLACK")
    public void canCreateOrderWithBlackColor() {
        Order order = new Order(
                "firstName",
                "lastName",
                "address",
                "Center",
                "12931839",
                1000,
                "10.10.2022",
                "-",
                new String[]{"BLACK"}
        );

        createOrder(order).then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("можно указать один из цветов — GREY")
    public void canCreateOrderWithGreyColor() {
        Order order = new Order(
                "firstName",
                "lastName",
                "address",
                "Center",
                "12931839",
                1000,
                "10.10.2022",
                "-",
                new String[]{"GREY"}
        );

        createOrder(order).then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("можно указать оба цвета")
    public void canCreateOrderWithGreyAndBlackColors() {
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
    }

    @Test
    @DisplayName("тело ответа содержит track")
    public void responseContainsTrack() {
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

        createOrder(order).then().assertThat().body("track", notNullValue());
    }

    @Step("Create order")
    public Response createOrder(Order order) {
        return given()
            .header("Content-type", "application/json")
            .and()
            .body(order)
            .post();
    }
}
