package order;

import io.restassured.response.ValidatableResponse;

import static constants.StatusCodes.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderAssertions {

    public void orderMadeSuccessfully(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().body("order.status", equalTo("done"))
                .and().statusCode(OK);

    }

    public void makeOrderWithoutToken(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("order.number", notNullValue())
                .and().statusCode(OK);

    }

    public void makeOrderWithoutIngredients(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(BAD_REQUEST);

    }

    public void makeOrderWithWrongHash(ValidatableResponse response){

        response.log().all().assertThat().statusCode(INTERNAL_SERVER_ERROR);

    }

}
