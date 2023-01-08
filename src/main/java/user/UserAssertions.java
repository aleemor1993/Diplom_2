package user;

import io.restassured.response.ValidatableResponse;
import user.requests.LoginUser;
import user.requests.RegisterUser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserAssertions {

    public void createdSuccessfully(ValidatableResponse response, RegisterUser registerUser){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(registerUser.getEmail()))
                .and().body("accessToken", notNullValue())
                .and().statusCode(200);

    }

    public void registerForbidden(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and().statusCode(403);
    }

    public void registerWithEmptyMandatoryFields(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    public void loggedInSuccessfully(ValidatableResponse response, LoginUser loginUser){

        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(loginUser.getEmail()))
                .and().body("accessToken", notNullValue())
                .and().statusCode(200);
    }

    public void loggedInUnsuccessfully(ValidatableResponse response){

        response.log().all().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    public String getValidAccessToken(ValidatableResponse response) {
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(200)
                .extract().path("accessToken");

    }

    public void getAllUserInfoSuccessfully(ValidatableResponse response, String email){
        response.log().all().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(email))
                .and().statusCode(200);
    }

    public String getUserNameSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(200)
                .and().extract().path("user.name");
    }

    public String getUserEmailSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().statusCode(200)
                .and().extract().path("user.email");
    }

    public ValidatableResponse deleteUserSuccessfully(ValidatableResponse response){
        return response.log().all().assertThat().body("success", equalTo(true))
                .and().body("message", equalTo("User successfully removed"))
                .and().statusCode(202);
    }

}
