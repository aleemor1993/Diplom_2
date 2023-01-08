package user;

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

    private final String INVALID_TOKEN = "Bearer 1";

    @Before
    public void registerUser(){
        RegisterUser registerUser = generator.random();
        ValidatableResponse response = client.register(registerUser);

        check.createdSuccessfully(response, registerUser);
        loginUser = new LoginUser(registerUser.getEmail(), registerUser.getPassword());
        accessToken = check.getValidAccessToken(client.login(loginUser));

    }

    @Test
    public void getUserInfoSuccessfully(){

        check.getAllUserInfoSuccessfully(client.getUserInfo(accessToken), loginUser.getEmail());
    }

    @Test
    public void changeNameSuccessfully(){

        client.changeUserInfoSuccessfully("name", accessToken, "Джон Лето Сноу Первый");

        ValidatableResponse response = client.getUserInfo(accessToken);

        Assert.assertEquals("Джон Лето Сноу Первый", check.getUserNameSuccessfully(response));

    }

    @Test
    public void changePasswordSuccessfully(){

        client.changeUserInfoSuccessfully("password", accessToken, "123456");

        loginUser.setPassword("123456");

        //TODO не хватает проверки на ответ

        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);
    }

    @Test
    public void changeEmailSuccessfully(){

        String newEmail = generator.getNewEmail(loginUser.getEmail());
        client.changeUserInfoSuccessfully("email",accessToken, newEmail);

        //TODO не хватает проверки на ответ
        loginUser.setEmail(newEmail);

        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);

    }

    //TODO попробовать обновить сразу три поля

    @Test
    public void changeNameWithoutToken(){

        ValidatableResponse response = client.changeFieldWithoutToken("name", "Джон Лето Сноу Первый");

        check.changeWithoutToken(response);

    }

    @Test
    public void changeEmailWithoutToken(){

        ValidatableResponse response = client.changeFieldWithoutToken
                ("email", generator.getNewEmail(loginUser.getEmail()));

        check.changeWithoutToken(response);

    }

    @Test
    public void changePasswordWithoutToken(){

        ValidatableResponse response = client.changeFieldWithoutToken("password", "555555");

        check.changeWithoutToken(response);

    }

    @Test
    public void changeNameWithExpiredToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("name", EXPIRED_TOKEN, "Джон Лето Сноу Первый");

        check.changeWithTokenExpired(response);

    }

    @Test
    public void changeEmailWithExpiredToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("email", EXPIRED_TOKEN, generator.getNewEmail(loginUser.getEmail()));

        check.changeWithTokenExpired(response);

    }

    @Test
    public void changePasswordWithExpiredToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("password", EXPIRED_TOKEN, "555555");

        check.changeWithTokenExpired(response);

    }

    @Test
    public void changeNameWithInvalidToken(){

        ValidatableResponse response = client.changeUserInfoSuccessfully
                ("name", INVALID_TOKEN, "Джон Лето Сноу Первый");

        check.changeWithTokenInvalid(response);

    }


    @After
    public void deleteUser(){

        ValidatableResponse response = client.deleteUserSuccessfully(accessToken, loginUser.getEmail());
        check.deleteUserSuccessfully(response);

    }

}
