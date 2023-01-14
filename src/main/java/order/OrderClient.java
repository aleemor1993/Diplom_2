package order;

import io.restassured.response.ValidatableResponse;
import order.requests.Ingredients;

import static io.restassured.RestAssured.given;

public class OrderClient {

    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String GET_INGREDIENTS = "/api/ingredients";
    protected final String ORDER = "/api/orders";

//TODO удалить, если не придумаю рефакторинга через получение списка ингредиентов
    public ValidatableResponse getIngredients(){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .when()
                .get(GET_INGREDIENTS).then().log().all();
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
