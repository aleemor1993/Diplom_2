package user;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import user.requests.RegisterUser;

public class RegisterTest {

    private final UserClient client = new UserClient();

    private final UserGenerator generator = new UserGenerator();

    private final UserAssertions check = new UserAssertions();


    @Test
    public void registerNewUserSuccessfully(){

        RegisterUser registerUser = generator.random();
        ValidatableResponse response = client.register(registerUser);

        check.createdSuccessfully(response, registerUser);

    }

    @Test
    public void registerExistingUser(){

        RegisterUser registerUser = generator.generic();
        ValidatableResponse response = client.register(registerUser);

        check.registerForbidden(response);

    }

    @Test
    public void registerWithEmptyName(){

        RegisterUser registerUser = generator.random();
        registerUser.setName("");
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    public void registerWithoutName(){

        RegisterUser registerUser = generator.random();
        registerUser.setName(null);
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    public void registerWithEmptyEmail(){

        RegisterUser registerUser = generator.random();
        registerUser.setEmail("");
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    public void registerWithoutEmail(){

        RegisterUser registerUser = generator.random();
        registerUser.setEmail(null);
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    public void registerWithEmptyPassword(){

        RegisterUser registerUser = generator.random();
        registerUser.setPassword("");
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    public void registerWithoutPassword(){

        RegisterUser registerUser = generator.random();
        registerUser.setPassword(null);
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);
    }

    @After
    //отсрочка запуска тестов во избежание Too many requests
    public void waitForSecond() throws InterruptedException {

        Thread.sleep(1000);
    }
}
