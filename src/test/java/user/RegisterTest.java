package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import user.requests.LoginUser;
import user.requests.RegisterUser;

public class RegisterTest {

    private final UserClient client = new UserClient();

    private final UserGenerator generator = new UserGenerator();

    private final UserAssertions check = new UserAssertions();

    boolean registered = false;

    RegisterUser registerUser;


    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    public void registerNewUserSuccessfully(){

        try {
            registerUser = generator.random();
            ValidatableResponse response = client.register(registerUser);

            check.createdSuccessfully(response, registerUser);

            registered = true;
        }
        catch (Exception e){
            System.out.println("Ошибка создания пользователя");
        }

    }

    @Test
    @DisplayName("Попытка регистрации существующего пользователя")
    public void registerExistingUser(){

        RegisterUser registerUser = generator.genericRegister();
        ValidatableResponse response = client.register(registerUser);

        check.registerForbidden(response);

    }

    @Test
    @DisplayName("Регистрация нового пользователя с пустым именем")
    public void registerWithEmptyName(){

        RegisterUser registerUser = generator.random();
        registerUser.setName("");
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    @DisplayName("Регистрация нового пользователя с отсутствующим полем name")
    public void registerWithoutName(){

        RegisterUser registerUser = generator.random();
        registerUser.setName(null);
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    @DisplayName("Регистрация нового пользователя с пустым email")
    public void registerWithEmptyEmail(){

        RegisterUser registerUser = generator.random();
        registerUser.setEmail("");
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    @DisplayName("Регистрация нового пользователя c отсутствующим полем email")
    public void registerWithoutEmail(){

        RegisterUser registerUser = generator.random();
        registerUser.setEmail(null);
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    @DisplayName("Регистрация нового пользователя с пустым паролем")
    public void registerWithEmptyPassword(){

        RegisterUser registerUser = generator.random();
        registerUser.setPassword("");
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);

    }

    @Test
    @DisplayName("Регистрация нового пользователя с отсутствующим полем пароль")
    public void registerWithoutPassword(){

        RegisterUser registerUser = generator.random();
        registerUser.setPassword(null);
        ValidatableResponse response = client.register(registerUser);

        check.registerWithEmptyMandatoryFields(response);
    }

    @After
    public void afterTests() throws InterruptedException {

        //отсрочка запуска тестов во избежание Too many requests
        Thread.sleep(2000);

        //удаление зарегистрированного пользователя при успешной регистрации
        if (registered){

            LoginUser loginUser = new LoginUser(registerUser.getEmail(), registerUser.getPassword());

            String accessToken = check.getValidAccessToken(client.login(loginUser));

            ValidatableResponse response = client.deleteUserSuccessfully(accessToken, registerUser.getEmail());
            check.deleteUserSuccessfully(response);
            registered = false;
        }
    }
}
