package user;

import io.restassured.response.ValidatableResponse;
import user.requests.LoginUser;
import user.requests.RegisterUser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static constants.StatusCodes.*;

public class UserAssertions {

    public void createdSuccessfully(ValidatableResponse response, RegisterUser registerUser){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(registerUser.getEmail()))
                .and().body("accessToken", notNullValue())
                .and().statusCode(OK);

    }

    public void registerForbidden(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and().statusCode(FORBIDDEN);
    }

    public void registerWithEmptyMandatoryFields(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(FORBIDDEN);
    }

    public void loggedInSuccessfully(ValidatableResponse response, LoginUser loginUser){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(loginUser.getEmail()))
                .and().body("accessToken", notNullValue())
                .and().statusCode(OK);
    }

    public void loggedInUnsuccessfully(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(UNAUTHORIZED);
    }

    public String getValidAccessToken(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(OK)
                .extract().path("accessToken");

    }

    public void getAllUserInfoSuccessfully(ValidatableResponse response, String email){
        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(email))
                .and().statusCode(OK);
    }

    public String getUserNameSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(OK)
                .and().extract().path("user.name");
    }

    public String getUserEmailSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(OK)
                .and().extract().path("user.email");
    }

    public ValidatableResponse deleteUserSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().body("message", equalTo("User successfully removed"))
                .and().statusCode(ACCEPTED);
    }

    public ValidatableResponse changeUserInfoSuccessfully(ValidatableResponse response, String field, String newValue){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user." + field, equalTo(newValue))
                .and().statusCode(OK);
    }

    public ValidatableResponse changeWithoutToken(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and().statusCode(UNAUTHORIZED);
    }

    public ValidatableResponse changeWithTokenExpired(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("jwt expired"))
                .and().statusCode(FORBIDDEN);
    }

    public ValidatableResponse changeWithTokenInvalid(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("jwt malformed"))
                .and().statusCode(FORBIDDEN);
    }

}
