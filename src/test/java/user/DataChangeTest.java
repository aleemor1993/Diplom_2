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

        client.changeUserNameSuccessfully(accessToken, "Джон Лето Сноу Первый");

        ValidatableResponse response = client.getUserInfo(accessToken);

        Assert.assertEquals("Джон Лето Сноу Первый", check.getUserNameSuccessfully(response));

    }

    @Test
    public void changePasswordSuccessfully(){

        client.changeUserPasswordSuccessfully(accessToken, "123456");

        loginUser.setPassword("123456");

        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);
    }

    @Test
    public void changeEmailSuccessfully(){

        String newEmail = generator.getNewEmail(loginUser.getEmail());
        client.changeUserEmailSuccessfully(accessToken, newEmail);

        loginUser.setEmail(newEmail);

        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);

    }

    @After
    public void deleteUser(){

        ValidatableResponse response = client.deleteUserSuccessfully(accessToken, loginUser.getEmail());
        check.deleteUserSuccessfully(response);

    }

}
