import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {

    OrderClient orderClient = new OrderClient();

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

        orderClient.createOrder(order).then().assertThat().statusCode(201);
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

        orderClient.createOrder(order).then().assertThat().statusCode(201);
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

        orderClient.createOrder(order).then().assertThat().statusCode(201);
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

        orderClient.createOrder(order).then().assertThat().statusCode(201);
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

        orderClient.createOrder(order).then().assertThat().body("track", notNullValue());
    }
}
