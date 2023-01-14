package order;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import user.UserAssertions;
import user.UserClient;
import user.UserGenerator;
import user.requests.LoginUser;

public class MakeOrderTest {

    private final UserClient userClient = new UserClient();

    private final UserGenerator userGenerator = new UserGenerator();

    private final UserAssertions userCheck = new UserAssertions();

    private final OrderClient orderClient = new OrderClient();

    private final OrderGenerator orderGenerator = new OrderGenerator();

    private final OrderAssertions orderCheck = new OrderAssertions();

    private String accessToken;


    @Test
    public void makeOrderSuccessfully(){

        //авторизация
        LoginUser loginUser = userGenerator.genericLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //создание заказа
        response = orderClient.makeOrder(accessToken, orderGenerator.genericRandomOrder());

        //проверка успешного создания
        orderCheck.orderMadeSuccessfully(response);

    }

    @Test
    public void makeOrderWithoutToken(){

        //создание заказа
        ValidatableResponse response = orderClient.makeOrderWithoutToken(orderGenerator.genericRandomOrder());

        //проверка успешного создания
        orderCheck.makeOrderWithoutToken(response);

    }

    @Test
    public void makeOrderWithoutIngredients(){

        //авторизация
        LoginUser loginUser = userGenerator.genericLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //создание заказа
        response = orderClient.makeOrder(accessToken, orderGenerator.genericEmptyOrder());

        //проверка ошибки создания
        orderCheck.makeOrderWithoutIngredients(response);

    }

    @Test
    public void makeOrderWithWrongHashIngredients(){

        //авторизация
        LoginUser loginUser = userGenerator.genericLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //создание заказа
        response = orderClient.makeOrder(accessToken, orderGenerator.genericWrongHashOrder());

        //проверка ошибки создания
        orderCheck.makeOrderWithWrongHash(response);

    }






}
