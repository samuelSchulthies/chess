package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.CreateRequest;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;

public class ClearServiceTest {
    static final UserService USER_SERVICE = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
    static final GameService GAME_SERVICE = new GameService(new MemoryGameDAO(), USER_SERVICE.getAuthTokenDAO());
    static final ClearService CLEAR_SERVICE = new ClearService(USER_SERVICE, GAME_SERVICE);
    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = USER_SERVICE.register(newUser);

        RegisterRequest newUser2 = new RegisterRequest("beth","321","bethisawesome@gmail.com");
        RegisterResult registeredUser2 = USER_SERVICE.register(newUser2);

        CreateRequest newGame = new CreateRequest("testGame");
        CreateRequest newGame2 = new CreateRequest("testGame");

        GAME_SERVICE.create(newGame, registeredUser.authToken());
        GAME_SERVICE.create(newGame2, registeredUser2.authToken());

        CLEAR_SERVICE.clear();

        Assertions.assertEquals(0, USER_SERVICE.userDataSize());
        Assertions.assertEquals(0, USER_SERVICE.authTokenSize());
        Assertions.assertEquals(0, GAME_SERVICE.gameDataSize());

    }
}