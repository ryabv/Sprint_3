import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {

    OrderClient orderClient = new OrderClient();

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

        orderClient.createOrder(order).then().assertThat().statusCode(201);
        orderClient.getOrdersList().then().assertThat().body("orders", notNullValue());
    }
}
