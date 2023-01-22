package order;

import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static user.UserGenerator.genericEmail;

public class OrderAssertions {

    public void orderMadeSuccessfully(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().body("order._id", notNullValue())
                .and().body("order.status", equalTo("done"))
                .and().body("order.owner.email", equalTo(genericEmail()))
                .and().statusCode(SC_OK);

    }

    public void makeOrderWithoutToken(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().statusCode(SC_OK);

    }

    public void makeOrderWithoutIngredients(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(SC_BAD_REQUEST);

    }

    public void makeOrderWithWrongHash(ValidatableResponse response){

        response.log().all().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    public void getUserOrdersSuccessfully(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body(containsString("orders"))
                .and().body("orders._id", notNullValue())
                .and().statusCode(SC_OK);

    }

    public ValidatableResponse getUserOrdersWithoutToken(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

}
