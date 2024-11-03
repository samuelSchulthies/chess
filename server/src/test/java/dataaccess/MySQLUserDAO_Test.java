package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.ClearService;
import service.GameService;
import service.UserService;

public class MySQLUserDAO_Test {

    @Test
    @DisplayName("Proper Create User")
    public void positiveCreateUser() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertNotNull(userService.getUserDAO().getUser(registeredUser.username()),
                "Registered user was not found in the database");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Create User")
    public void negativeCreateUser() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        userService.register(newUser);

        RegisterRequest newUserNameAlreadyTaken = new RegisterRequest("sally","321","sallyisnotawesome@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUserNameAlreadyTaken));

        RegisterRequest newUserNameNull = new RegisterRequest("",null,"sallyisnotawesome@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUserNameNull));

        clearService.clear();
    }

    @Test
    @DisplayName("Proper Get User")
    public void positiveGetUser() throws DataAccessException {
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
