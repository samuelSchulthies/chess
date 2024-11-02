package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.UserService;

public class MySQLUserDAO_Test {

    @Test
    @DisplayName("Proper Create User")
    public void positiveCreateUser() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertNotNull(userService.getUserDAO().getUser(registeredUser.username()),
                "Registered user was not found in the database");
    }

    @Test
    @DisplayName("Improper Create User")
    public void negativeCreateUser() throws DataAccessException {

    }

    @Test
    @DisplayName("Proper Get User")
    public void positiveUser() throws DataAccessException {
    }

    @Test
    @DisplayName("Improper Get User")
    public void negativeGetUser() throws DataAccessException {

    }

    @Test
    @DisplayName("Clear User")
    public void clearUser() throws DataAccessException {

    }

}
