package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import user.requests.LoginUser;

public class LoginTest {

    private final UserClient client = new UserClient();

    private final UserGenerator generator = new UserGenerator();

    private final UserAssertions check = new UserAssertions();


    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    public void loggedInSuccessfully(){

        LoginUser loginUser = generator.genericLogin();
        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);

    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loggedInWithWrongPassword(){

        LoginUser loginUser = generator.genericLogin();
        loginUser.setPassword("222222");
        ValidatableResponse response = client.login(loginUser);

        check.loggedInUnsuccessfully(response);

    }

    @Test
    @DisplayName("Авторизация с несуществующим email")
    public void loggedInWithWrongEmail(){

        LoginUser loginUser = generator.genericLogin();
        loginUser.setEmail(loginUser.getEmail() + "1");
        ValidatableResponse response = client.login(loginUser);

        check.loggedInUnsuccessfully(response);
    }

}
