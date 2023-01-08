package user;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import user.requests.LoginUser;

public class LoginTest {

    private final UserClient client = new UserClient();

    private final UserGenerator generator = new UserGenerator();

    private final UserAssertions check = new UserAssertions();


    @Test
    public void loggedInSuccessfully(){

        LoginUser loginUser = generator.genericLogin();
        ValidatableResponse response = client.login(loginUser);

        check.loggedInSuccessfully(response, loginUser);

    }

    @Test
    public void loggedInWithWrongPassword(){

        LoginUser loginUser = generator.genericLogin();
        loginUser.setPassword("222222");
        ValidatableResponse response = client.login(loginUser);

        check.loggedInUnsuccessfully(response);

    }

    @Test
    public void loggedInWithWrongEmail(){

        LoginUser loginUser = generator.genericLogin();
        loginUser.setEmail(loginUser.getEmail() + "1");
        ValidatableResponse response = client.login(loginUser);

        check.loggedInUnsuccessfully(response);
    }


}
