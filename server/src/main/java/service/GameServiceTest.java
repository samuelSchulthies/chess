package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.*;

public class GameServiceTest {

    @Test
    @DisplayName("Positive Create Result")
    public void positiveCreate() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game", registeredUser.authToken());
        CreateResult createdGame = gameService.create(newGame);

        Assertions.assertNotNull(gameService.getGameDAO().getGame(createdGame.gameID()),
                "Game with given gameID was not found");
    }

    @Test
    @DisplayName("Negative Create Result")
    public void negativeCreate() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGameEmptyName = new CreateRequest("", registeredUser.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.create(newGameEmptyName));

        LogoutRequest logoutUser = new LogoutRequest(registeredUser.authToken());
        userService.logout(logoutUser);

        CreateRequest newGameNoAuth = new CreateRequest("Sally's game", registeredUser.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.create(newGameNoAuth));

    }

    @Test
    @DisplayName("Positive Join Result")
    public void positiveJoin() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("benson","321","benisawesome@gmail.com");
        RegisterResult registeredUser2 = userService.register(newUser2);

        CreateRequest newGame = new CreateRequest("Sally's game", registeredUser.authToken());
        CreateResult createdGame = gameService.create(newGame);

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID(), registeredUser.authToken());
        gameService.join(newPlayer);
        JoinRequest newPlayer2 = new JoinRequest("WHITE", createdGame.gameID(), registeredUser2.authToken());
        gameService.join(newPlayer2);

        Assertions.assertEquals(registeredUser.username(),
                gameService.getGameDAO().getGame(createdGame.gameID()).blackUsername(),
                "Joined player did not take BLACK's spot");

        Assertions.assertEquals(registeredUser2.username(),
                gameService.getGameDAO().getGame(createdGame.gameID()).whiteUsername(),
                "Joined player did not take WHITE's spot");
    }

    @Test
    @DisplayName("Negative Join Result")
    public void negativeJoin() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("benson","321","benisawesome@gmail.com");
        RegisterResult registeredUser2 = userService.register(newUser2);
        RegisterRequest newUser3 = new RegisterRequest("jason","312","jasonisawesome@gmail.com");
        RegisterResult registeredUser3 = userService.register(newUser3);

        CreateRequest newGame = new CreateRequest("Black taken and full game", registeredUser.authToken());
        CreateResult createdGame = gameService.create(newGame);

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID(), registeredUser.authToken());
        gameService.join(newPlayer);
        JoinRequest newPlayer2 = new JoinRequest("BLACK", createdGame.gameID(), registeredUser2.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer2),
                "Player joined black when black was taken");
        JoinRequest newPlayer2Retry = new JoinRequest("WHITE", createdGame.gameID(), registeredUser2.authToken());
        gameService.join(newPlayer2Retry);
        JoinRequest newPlayer3 = new JoinRequest("WHITE", createdGame.gameID(), registeredUser3.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer3),
                "Player joined full game");


        CreateRequest newGame2 = new CreateRequest("White taken", registeredUser.authToken());
        CreateResult createdGame2 = gameService.create(newGame2);

        JoinRequest newPlayer5 = new JoinRequest("WHITE", createdGame2.gameID(), registeredUser.authToken());
        gameService.join(newPlayer5);
        JoinRequest newPlayer6 = new JoinRequest("WHITE", createdGame2.gameID(), registeredUser2.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer6),
                "Player joined white when white was taken");

        JoinRequest newPlayer7 = new JoinRequest("BLACK", 2, registeredUser2.authToken());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer7),
                "Player joined non-existent game");
    }

    @Test
    @DisplayName("Positive List Result")
    public void positiveList() throws DataAccessException {
        throw new RuntimeException("postive list test not implemented");
    }

    @Test
    @DisplayName("Negative List Result")
    public void negativeList() throws DataAccessException {
        throw new RuntimeException("negative list test not implemented");
    }
}