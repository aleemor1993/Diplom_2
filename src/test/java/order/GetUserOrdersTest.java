package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import user.UserAssertions;
import user.UserClient;
import user.UserGenerator;
import user.requests.LoginUser;

public class GetUserOrdersTest {

    private final UserClient userClient = new UserClient();

    private final UserGenerator userGenerator = new UserGenerator();

    private final UserAssertions userCheck = new UserAssertions();

    private final OrderClient orderClient = new OrderClient();

    private final OrderAssertions orderCheck = new OrderAssertions();

    private String accessToken;

    @Test
    @DisplayName("Успешное получение заказов пользователя с существующими заказами")
    public void getUserOrdersSuccessfully(){

        //авторизация
        LoginUser loginUser = userGenerator.genericLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //получение последних заказов
        response = orderClient.getUserOrders(accessToken);

        //проверка успешного получения
        orderCheck.getUserOrdersSuccessfully(response);

    }

    @Test
    @DisplayName("Успешное получение заказов пользователя с существующими заказами")
    public void getEmptyUserOrdersSuccessfully(){

        //авторизация
        LoginUser loginUser = userGenerator.emptyOrdersLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //получение последних заказов
        response = orderClient.getUserOrders(accessToken);

        //проверка успешного получения
        orderCheck.getUserOrdersSuccessfully(response);

    }

    @Test
    @DisplayName("Получение заказов пользователя с некорректным токеном")
    public void getUserOrdersWithoutToken(){

        //получение заказов
        ValidatableResponse response = orderClient.getUserOrdersWithoutToken();

        //проверка ошибки получения
        orderCheck.getUserOrdersWithoutToken(response);
    }

    @Test
    @DisplayName("Получение заказов пользователя с некорректным токеном")
    public void getUserOrdersWithoutExistingOrders(){
        //получение заказов
        ValidatableResponse response = orderClient.getUserOrdersWithoutToken();

        //проверка ошибки получения
        orderCheck.getUserOrdersWithoutToken(response);
    }

    @After
    public void waitFor() throws InterruptedException {

        //задержка после выполнения во избежание too many requests
        Thread.sleep(2000);

    }


}
