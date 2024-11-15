package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.ClearService;
import service.GameService;
import service.UserService;
import exception.DataAccessException;

public class MySQLAuthTokenDAOTest {

    @Test
    @DisplayName("Proper Create Auth")
    public void positiveCreateAuth() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertNotNull(userService.getAuthTokenDAO().getAuth(registeredUser.authToken()),
                "Created authToken was not found in the database");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Create Auth")
    public void negativeCreateAuth() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        String usernameOneCharTooBig =
                """
                12345678901234567890123456789012345678901234567890123456789012345678901234567890
                12345678901234567890123456789012345678901234567890123456789012345678901234567890
                12345678901234567890123456789012345678901234567890123456789012345678901234567890
                1234567801234
                """;

        RegisterRequest newUser = new RegisterRequest(usernameOneCharTooBig,"123","sallyisawesome@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUser),
                "Username too long to generate authToken");

        clearService.clear();
    }

    @Test
    @DisplayName("Proper Get Auth")
    public void positiveLogin() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertNotNull(userService.getAuthTokenDAO().getAuth(registeredUser.authToken()),
                "Authtoken not found");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Get Auth")
    public void negativeGetAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Proper DeleteAuth")
    public void positiveDeleteAuth() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        userService.getAuthTokenDAO().deleteAuth(registeredUser.authToken());

        Assertions.assertNull(userService.getAuthTokenDAO().getAuth(registeredUser.authToken()),
                "Deleted auth was found in the database");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper DeleteAuth")
    public void negativeDeleteAuth() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        userService.getAuthTokenDAO().deleteAuth("");

        Assertions.assertNotNull(userService.getAuthTokenDAO().getAuth(registeredUser.authToken()),
                "Deleted incorrect authToken");

        clearService.clear();

    }

    @Test
    @DisplayName("Clear Auth")
    public void clearAuth() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("jim","321","jimisawesome@gmail.com");
        RegisterResult registeredUser2 = userService.register(newUser2);

        userService.getAuthTokenDAO().clear();
        Assertions.assertNull(userService.getAuthTokenDAO().getAuth(registeredUser.authToken()),
                "retrieved non-existent authData");
        Assertions.assertNull(userService.getAuthTokenDAO().getAuth(registeredUser2.authToken()),
                "retrieved non-existent authData");

        clearService.clear();
    }

}
