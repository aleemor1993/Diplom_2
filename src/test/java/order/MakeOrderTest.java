package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.requests.Ingredients;
import order.responses.orders.get.UserOrders;
import org.junit.After;
import org.junit.Assert;
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
    @DisplayName("Успешное создание заказа")
    public void makeOrderSuccessfully(){

        //авторизация
        LoginUser loginUser = userGenerator.genericLogin();
        ValidatableResponse response = userClient.login(loginUser);

        userCheck.loggedInSuccessfully(response, loginUser);

        //получение токена доступа
        accessToken = userCheck.getValidAccessToken(userClient.login(loginUser));

        //получение данных о текущем состоянии всех заказов по всем пользователям и по текущему
        int allTotal = orderClient.getAllUsersOrders().getTotal();
        int allTotalToday = orderClient.getAllUsersOrders().getTotalToday();

        int userTotal = orderClient.getOrders(accessToken).getTotal();
        int userTotalToday = orderClient.getOrders(accessToken).getTotalToday();

        Ingredients newOrderIngredients = orderGenerator.getListOfIngredients();

        //создание заказа
        response = orderClient.makeOrder(accessToken, newOrderIngredients);

        //проверка успешного создания
        orderCheck.orderMadeSuccessfully(response);

        UserOrders allUsersOrders = orderClient.getAllUsersOrders();

        //проверка, что общее количество заказов увеличено и новый заказ создан с нужными ингредиентами
        Assert.assertEquals(allTotal+1, allUsersOrders.getTotal());
        Assert.assertEquals(allTotalToday+1, allUsersOrders.getTotalToday());
        Assert.assertEquals(newOrderIngredients.getIngredients(), allUsersOrders.getOrders().get(0).getIngredients());

        //заказ отображается в заказах текущего пользователя
        UserOrders currentUserOrders = orderClient.getOrders(accessToken);
        Assert.assertEquals(userTotal+1, currentUserOrders.getTotal());
        Assert.assertEquals(userTotalToday+1, currentUserOrders.getTotalToday());
        Assert.assertEquals(newOrderIngredients.getIngredients(), currentUserOrders.getOrders().get(currentUserOrders.getOrders().size()-1).getIngredients());

    }


    @Test
    @DisplayName("Создание заказа пользователя без токена")
    public void makeOrderWithoutToken(){

        //кейс некорректный, поскольку сделать заказ без авторизации должно быть нельзя, но соответствует требуемой документации API
        //создание заказа
        ValidatableResponse response = orderClient.makeOrderWithoutToken(orderGenerator.getListOfIngredients());

        //проверка успешного создания
        orderCheck.makeOrderWithoutToken(response);

    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
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
    @DisplayName("Создание заказа с неверным хэшем ингредиента")
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

    @After
    public void waitFor() throws InterruptedException {

        //задержка после выполнения во избежание too many requests
        Thread.sleep(3000);

    }
}
