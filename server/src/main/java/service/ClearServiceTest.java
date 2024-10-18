package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.CreateRequest;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

public class ClearServiceTest {
    static final UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
    static final GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());
    static final ClearService clearService = new ClearService(userService, gameService);
    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        RegisterRequest newUser2 = new RegisterRequest("beth","321","bethisawesome@gmail.com");
        RegisterResult registeredUser2 = userService.register(newUser2);

        CreateRequest newGame = new CreateRequest("testGame", registeredUser.authToken());
        CreateRequest newGame2 = new CreateRequest("testGame", registeredUser2.authToken());

        gameService.create(newGame);
        gameService.create(newGame2);

        clearService.clear();

        Assertions.assertEquals(0, userService.userDataSize());
        Assertions.assertEquals(0, userService.authTokenSize());
        Assertions.assertEquals(0, gameService.gameDataSize());

    }
}