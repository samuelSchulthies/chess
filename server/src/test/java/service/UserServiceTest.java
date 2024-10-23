package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.*;

public class UserServiceTest {

    @Test
    @DisplayName("Proper Register Result")
    public void positiveRegister() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        Assertions.assertNotNull(userService.getUserDAO().getUser(registeredUser.username()),
                "Registered user was not found in the database");

    }

    @Test
    @DisplayName("Improper Register Result")
    public void negativeRegister() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        userService.register(newUser);

        RegisterRequest newUserNameAlreadyTaken = new RegisterRequest("sally","321","sallyisnotawesome@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUserNameAlreadyTaken));

        RegisterRequest newUserNameNull = new RegisterRequest("",null,"sallyisnotawesome@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUserNameNull));

    }

    @Test
    @DisplayName("Proper Login Result")
    public void positiveLogin() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        LogoutRequest logOutUser = new LogoutRequest();
        userService.logout(logOutUser, registeredUser.authToken());

        LoginRequest loginUser = new LoginRequest("sally", "123");
        LoginResult loggedInUser = userService.login(loginUser);
        String authTokenStorage = loggedInUser.authToken();

        Assertions.assertNotNull(userService.getAuthTokenDAO().getAuth(authTokenStorage),
                "User logged in but authToken is not found");
    }

    @Test
    @DisplayName("Improper Login Result")
    public void negativeLogin() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        LogoutRequest logOutUser = new LogoutRequest();
        userService.logout(logOutUser, registeredUser.authToken());

        LoginRequest loginUserBadUsername = new LoginRequest("saly", "123");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(loginUserBadUsername));

        LoginRequest loginUserBadPassword = new LoginRequest("sally", "1234");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(loginUserBadPassword));

    }

    @Test
    @DisplayName("Proper Logout Result")
    public void positiveLogout() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        LogoutRequest logOutUser = new LogoutRequest();
        String authTokenStorage = registeredUser.authToken();
        userService.logout(logOutUser, registeredUser.authToken());

        Assertions.assertNull(userService.getAuthTokenDAO().getAuth(authTokenStorage),
                "AuthToken was not removed");

    }

    @Test
    @DisplayName("Improper Logout Result")
    public void negativeLogout() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        LogoutRequest logOutUser = new LogoutRequest();

        userService.logout(logOutUser, registeredUser.authToken());

        try {
            userService.logout(logOutUser, registeredUser.authToken());
        }
        catch (DataAccessException e){
            Assertions.assertEquals("dataaccess.DataAccessException: invalid authtoken", e.toString(),
                    "Invalid authtoken exception did not return");
        }

    }
}