package order;

import io.restassured.response.ValidatableResponse;
import order.requests.Ingredients;
import order.responses.ingredients.IngredientsList;
import order.responses.orders.get.UserOrders;

import static io.restassured.RestAssured.given;

public class OrderClient {

    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String GET_INGREDIENTS = "/api/ingredients";
    protected final String ORDER = "/api/orders";

    protected final String GET_ALL_ORDERS = "/api/orders/all";

    //получение списка валидных ингредиентов
    public IngredientsList getIngredients(){
        return  given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .when()
                .log().all().get(GET_INGREDIENTS).body().as(IngredientsList.class);
    }

    //получение списка 50 последних заказов всех пользователей
    public UserOrders getAllUsersOrders(){
        return  given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .when()
                .log().all().get(GET_ALL_ORDERS).body().as(UserOrders.class);
    }

    public UserOrders getOrders(String accessToken){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .when()
                .log().all().get(ORDER).body().as(UserOrders.class);
    }



    public ValidatableResponse makeOrder(String accessToken, Ingredients order){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .body(order)
                .when()
                .post(ORDER).then().log().all();
    }

    public ValidatableResponse makeOrderWithoutToken(Ingredients order){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(order)
                .when()
                .post(ORDER).then().log().all();
    }

    public ValidatableResponse getUserOrders(String accessToken){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .when()
                .get(ORDER).then().log().all();
    }

    public ValidatableResponse getUserOrdersWithoutToken(){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .when()
                .get(ORDER).then().log().all();
    }

}
