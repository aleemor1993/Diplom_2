package user;

import io.restassured.response.ValidatableResponse;
import user.requests.LoginUser;
import user.requests.RegisterUser;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserAssertions {

    public void createdSuccessfully(ValidatableResponse response, RegisterUser registerUser){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(registerUser.getEmail()))
                .and().body("accessToken", notNullValue())
                .and().statusCode(SC_OK);

    }

    public void registerForbidden(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and().statusCode(SC_FORBIDDEN);
    }

    public void registerWithEmptyMandatoryFields(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(SC_FORBIDDEN);
    }

    public void loggedInSuccessfully(ValidatableResponse response, LoginUser loginUser){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(loginUser.getEmail()))
                .and().body("accessToken", notNullValue())
                .and().statusCode(SC_OK);
    }

    public void loggedInUnsuccessfully(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    public String getValidAccessToken(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK)
                .extract().path("accessToken");

    }

    public String getUserNameSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK)
                .and().extract().path("user.name");
    }

    public ValidatableResponse deleteUserSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().body("message", equalTo("User successfully removed"))
                .and().statusCode(SC_ACCEPTED);
    }

    public ValidatableResponse changeUserInfoSuccessfully(ValidatableResponse response, String field, String newValue){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user." + field, equalTo(newValue))
                .and().statusCode(SC_OK);
    }

    public ValidatableResponse changeWithoutToken(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    public ValidatableResponse changeWithTokenExpired(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("jwt expired"))
                .and().statusCode(SC_FORBIDDEN);
    }

    public ValidatableResponse changeWithTokenInvalid(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("jwt malformed"))
                .and().statusCode(SC_FORBIDDEN);
    }

}
