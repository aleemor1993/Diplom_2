package user;

import user.requests.LoginUser;
import user.requests.RegisterUser;

import java.util.Random;

public class UserGenerator {

    public RegisterUser genericRegister(){
        return new RegisterUser("johnnletit169snow@yandex.ru", "111111", "Александра Александрова");
    }

    public RegisterUser random(){
        return new RegisterUser("johnnletit" + new Random().nextInt(200) + "snow@yandex.ru",
                "111111", "Джон Лето Сноу");
    }

    public LoginUser genericLogin(){

        return new LoginUser("johnnletit169snow@yandex.ru", "111111");
    }

    public String getNewEmail(String email) {

        return email + new Random().nextInt(200);

    }
}
