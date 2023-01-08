package user;

import io.restassured.response.ValidatableResponse;
import user.requests.LoginUser;
import user.requests.RegisterUser;

import static io.restassured.RestAssured.given;

public class UserClient {

    protected final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String LOGIN = "/api/auth/login";
    protected final String REGISTRATION = "/api/auth/register";

    protected final String USER = "/api/auth/user";


    public ValidatableResponse register(RegisterUser registerUser){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(registerUser)
                .when()
                .post(REGISTRATION).then().log().all();
    }

    public ValidatableResponse login(LoginUser loginUser){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .baseUri(BASE_URI)
                .body(loginUser)
                .when()
                .post(LOGIN).then().log().all();

    }

    public ValidatableResponse getUserInfo(String accessToken){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .when()
                .get(USER).then().log().all();
    }

    public ValidatableResponse changeUserNameSuccessfully(String accessToken, String newName){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .body(String.format("{\"name\": \"%s\"}", newName))
                .when()
                .patch(USER).then().log().all();
    }

    public ValidatableResponse changeUserPasswordSuccessfully(String accessToken, String newPassword){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .body(String.format("{\"password\": \"%s\"}", newPassword))
                .when()
                .patch(USER).then().log().all();
    }

    public ValidatableResponse changeUserEmailSuccessfully(String accessToken, String newEmail){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .body(String.format("{\"email\": \"%s\"}", newEmail))
                .when()
                .patch(USER).then().log().all();
    }

    public ValidatableResponse deleteUserSuccessfully(String accessToken, String email){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .body(String.format("{\"email\": \"%s\"}", email))
                .when()
                .delete(USER).then().log().all();
    }



}
