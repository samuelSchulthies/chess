package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

public class UserServiceTest {

    @Test
    @DisplayName("Positive Register Result")
    public void positiveRegister() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertEquals(newUser.username(),
                userService.getUserDAO().getUser(registeredUser.username()).username(),
                "Registered user was not found in the database");

    }

    @Test
    @DisplayName("Negative Register Result")
    public void negativeRegister(){
        throw new RuntimeException("negative register test not implemented");
    }

    @Test
    @DisplayName("Positive Login Result")
    public void positiveLogin(){
        throw new RuntimeException("positive login test not implemented");
    }

    @Test
    @DisplayName("Negative Login Result")
    public void negativeLogin(){
        throw new RuntimeException("negative login test not implemented");
    }

    @Test
    @DisplayName("Positive Logout Result")
    public void positiveLogout(){
        throw new RuntimeException("positive logout test not implemented");
    }

    @Test
    @DisplayName("Negative Logout Result")
    public void negativeLogout(){
        throw new RuntimeException("negative logout test not implemented");
    }
}