package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.requests.LoginUser;
import user.requests.RegisterUser;

public class DataChangeTest {

    private final UserClient client = new UserClient();

    private final UserGenerator generator = new UserGenerator();

    private final UserAssertions check = new UserAssertions();

    private LoginUser loginUser;

    private String accessToken;

    private final String EXPIRED_TOKEN =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYzYmIxMzZiYzE2MWRhMDAxYjdhNWZiMCIsImlhdCI6MTY3MzIwNTMxMSwiZXhwIjoxNjczMjA2NTExfQ.VJ7RBlRdbH0_sDBHx3EXhdInrR4kOCD0SnzCA8iFyOc";

    @Before
    public void registerUser(){
        RegisterUser registerUser = generator.random();
        ValidatableResponse response = client.register(registerUser);

        check.createdSuccessfully(response, registerUser);
        loginUser = new LoginUser(registerUser.getEmail(), registerUser.getPassword());
        accessToken = check.getValidAccessToken(client.login(loginUser));

    }

    @Test
    @DisplayName("Успешное получение данных существующего пользователя")
    public void getUserInfoSuccessfully(){

        check.getAllUserInfoSuccessfully(client.getUserInfo(accessToken), loginUser.getEmail());
    }

    @Test
    @DisplayName("Успешное изменение имени пользователя")
    public void changeNameSuccessfully(){

        ValidatableResponse response = client.changeUserInfoSuccessfully("name", accessToken, "Джон Лето Сноу Первый");

        check.changeUserInfoSuccessfully(response, "name", "Джон Лето Сноу Первый");

        response = client.getUserInfo(accessToken);

        Assert.assertEquals("Джон Лето Сноу Первый", check.getUserNameSuccessfully(response));

    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя")
    public void changePasswordSuccessfully(){

        client.changeUserInfoSuccessfully("password", accessToken, "123456");

        loginUser.setPassword("123456");

        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);
    }

    @Test
    @DisplayName("Успешное изменение email пользователя")
    public void changeEmailSuccessfully(){

        String newEmail = generator.getNewEmail(loginUser.getEmail());
        ValidatableResponse response = client.changeUserInfoSuccessfully("email",accessToken, newEmail);

        check.changeUserInfoSuccessfully(response, "email", newEmail);

        loginUser.setEmail(newEmail);

        response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);

    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeNameWithoutToken(){

        ValidatableResponse response = client.changeFieldWithoutToken("name", "Джон Лето Сноу Первый");

        check.changeWithoutToken(response);

    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    public void changeEmailWithoutToken(){

        ValidatableResponse response = client.changeFieldWithoutToken
                ("email", generator.getNewEmail(loginUser.getEmail()));

        check.changeWithoutToken(response);

    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    public void changePasswordWithoutToken(){

        ValidatableResponse response = client.changeFieldWithoutToken("password", "555555");

        check.changeWithoutToken(response);

    }

    @Test
    @DisplayName("Изменение имени пользователя с истекшим токеном")
    public void changeNameWithExpiredToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("name", EXPIRED_TOKEN, "Джон Лето Сноу Первый");

        check.changeWithTokenExpired(response);

    }

    @Test
    @DisplayName("Изменение email пользователя с истекшим токеном")
    public void changeEmailWithExpiredToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("email", EXPIRED_TOKEN, generator.getNewEmail(loginUser.getEmail()));

        check.changeWithTokenExpired(response);

    }

    @Test
    @DisplayName("Изменение пароля пользователя с истекшим токеном")
    public void changePasswordWithExpiredToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("password", EXPIRED_TOKEN, "555555");

        check.changeWithTokenExpired(response);

    }

    @Test
    @DisplayName("Изменение имени пользователя с невалидным токеном")
    public void changeNameWithInvalidToken(){

        String INVALID_TOKEN = "Bearer 1";
        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("name", INVALID_TOKEN, "Джон Лето Сноу Первый");

        check.changeWithTokenInvalid(response);

    }

    @After
    public void deleteUser() throws InterruptedException {

        ValidatableResponse response = client.deleteUserSuccessfully(accessToken, loginUser.getEmail());
        check.deleteUserSuccessfully(response);

        //задержка после выполнения во избежание too many requests
        Thread.sleep(1000);

    }

}
