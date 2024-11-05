package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.ClearService;
import service.GameService;
import service.UserService;

public class MySQL_UserDAO_Test {

    @Test
    @DisplayName("Proper Create User")
    public void positiveCreateUser() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertNotNull(userService.getUserDAO().getUser(registeredUser.username()),
                "Registered user was not found in the database");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Create User")
    public void negativeCreateUser() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

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
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        UserData newUserTypeCast = new UserData(newUser.username(), newUser.password(), newUser.email());

        Assertions.assertEquals(newUserTypeCast.username(),
                userService.getUserDAO().getUser(registeredUser.username()).username(),
                "Registered user's username was not found in the database");
        Assertions.assertEquals(newUserTypeCast.email(),
                userService.getUserDAO().getUser(registeredUser.username()).email(),
                "Registered user's email was not found in the database");
        Assertions.assertTrue(BCrypt.checkpw(newUserTypeCast.password(),
                        userService.getUserDAO().getUser(registeredUser.username()).password()),
                "Registered user's password was not found in the database");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Get User")
    public void negativeGetUser() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        userService.register(newUser);

        Assertions.assertNull(userService.getUserDAO().getUser("saly"), "retrieved non-existent user");

        clearService.clear();
    }

    @Test
    @DisplayName("Clear User")
    public void clearUser() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("jim","321","jimisawesome@gmail.com");
        RegisterResult registeredUser2 = userService.register(newUser2);

        userService.getUserDAO().clear();
        Assertions.assertNull(userService.getUserDAO().getUser(registeredUser.username()),
                "retrieved non-existent user");
        Assertions.assertNull(userService.getUserDAO().getUser(registeredUser2.username()),
                "retrieved non-existent user");

        clearService.clear();
    }

}
