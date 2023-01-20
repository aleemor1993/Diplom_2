package order;

import io.restassured.response.ValidatableResponse;
import order.responses.ingredients.Ingredient;
import order.responses.ingredients.IngredientsList;
import org.junit.Test;
import user.UserAssertions;
import user.UserClient;
import user.UserGenerator;
import user.requests.LoginUser;

import java.util.ArrayList;
import java.util.List;

public class GetUserOrders {

    private final UserClient userClient = new UserClient();

    private final UserGenerator userGenerator = new UserGenerator();

    private final UserAssertions userCheck = new UserAssertions();

    private final OrderClient orderClient = new OrderClient();

    private final OrderGenerator orderGenerator = new OrderGenerator();

    private final OrderAssertions orderCheck = new OrderAssertions();

    private String accessToken;

    @Test
    public void getUserOrdersSuccessfully(){

        //авторизация
        LoginUser loginUser = userGenerator.genericLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //получение последних заказов
        response = orderClient.getUserOrders(accessToken);

        //проверка успешного создания
        orderCheck.getUserOrdersSuccessfully(response);

    }

    @Test
    public void getUserOrdersWithoutToken(){

        //получение заказов
        ValidatableResponse response = orderClient.getUserOrdersWithoutToken();

        //проверка ошибки получения
        orderCheck.getUserOrdersWithoutToken(response);

    }

}
