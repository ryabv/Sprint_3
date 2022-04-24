import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    CourierClient courierClient = new CourierClient();

    @Test
    @DisplayName("курьера можно создать со статусом 201 и ok: true")
    public void canCreateCourier() {
        Courier courier = new Courier("HermioneTheSmartest", "Avada Kedavra", "Hermione");

        try {
            courierClient.createCourier(courier)
                    .then().assertThat().statusCode(201)
                    .and().assertThat().body("ok", equalTo(true));
        } finally {
            courierClient.deleteCourier(courier);
        }
    }

    @Test
    @DisplayName("нельзя создать двух одинаковых курьеров")
    public void cannotCreateSimilarCouriers() {
        Courier courier = new Courier("Kolya", "abcd");

        try {
            courierClient.createCourier(courier).then().assertThat().statusCode(201);
            courierClient.createCourier(courier).then().assertThat().statusCode(409);
        } finally {
            courierClient.deleteCourier(courier);
        }
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void needAllRequiredFieldsToCreateCourier() {
        Courier courierWithoutLogin = new Courier();
        Courier courierWithoutPassword = new Courier("Sergey");
        Courier courierWithAllRequiredFields = new Courier("Sergey", "lalala");

        try {
            courierClient.createCourier(courierWithoutLogin).then().assertThat().statusCode(400);
            courierClient.createCourier(courierWithoutPassword).then().assertThat().statusCode(400);
            courierClient.createCourier(courierWithAllRequiredFields).then().assertThat().statusCode(201);
        } finally {
            courierClient.deleteCourier(courierWithAllRequiredFields);
        }
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля. Не передан логин")
    public void needLoginFieldToCreateCourier() {
        Courier courierWithoutLogin = new Courier();
        Courier courierWithAllRequiredFields = new Courier("Sergey", "lalala");

        try {
            courierClient.createCourier(courierWithoutLogin).then().assertThat().statusCode(400);
            courierClient.createCourier(courierWithAllRequiredFields).then().assertThat().statusCode(201);
        } finally {
            courierClient.deleteCourier(courierWithAllRequiredFields);
        }
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля. Не передан пароль")
    public void needPasswordFieldToCreateCourier() {
        Courier courierWithoutPassword = new Courier("Sergey");
        Courier courierWithAllRequiredFields = new Courier("Sergey", "lalala");

        try {
            courierClient.createCourier(courierWithoutPassword).then().assertThat().statusCode(400);
            courierClient.createCourier(courierWithAllRequiredFields).then().assertThat().statusCode(201);
        } finally {
            courierClient.deleteCourier(courierWithAllRequiredFields);
        }
    }

    @Test
    @DisplayName("если нет логина, запрос возвращает ошибку")
    public void throwErrorIfMissingLoginField() {
        Courier courierWithoutLogin = new Courier();

        courierClient.createCourier(courierWithoutLogin)
                .then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если нет пароля, запрос возвращает ошибку")
    public void throwErrorIfMissingPasswordField() {
        Courier courierWithoutPassword = new Courier("Petr");

        courierClient.createCourier(courierWithoutPassword)
                .then().assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void throwErrorIfCreatingCourierWithExistingLogin() {
        Courier courier = new Courier("Ivanov", "123");

        try {
            courierClient.createCourier(courier).then().statusCode(201);
            courierClient.createCourier(courier)
                    .then().assertThat()
                    .body("message", equalTo("Этот логин уже используется"));
        } finally {
            courierClient.deleteCourier(courier);
        }
    }
}
