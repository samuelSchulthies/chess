package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthTokenDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.*;

import java.util.ArrayList;

public class GameServiceTest {

    @Test
    @DisplayName("Positive Create Result")
    public void positiveCreate() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

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

        CreateRequest newGameEmptyName = new CreateRequest("");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.create(newGameEmptyName, registeredUser.authToken()));

        LogoutRequest logoutUser = new LogoutRequest();
        userService.logout(registeredUser.authToken());

        CreateRequest newGameNoAuth = new CreateRequest("Sally's game");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.create(newGameNoAuth, registeredUser.authToken()));

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

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID());
        gameService.join(newPlayer, registeredUser.authToken());
        JoinRequest newPlayer2 = new JoinRequest("WHITE", createdGame.gameID());
        gameService.join(newPlayer2, registeredUser2.authToken());

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

        CreateRequest newGame = new CreateRequest("Black taken and full game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID());
        gameService.join(newPlayer, registeredUser.authToken());
        JoinRequest newPlayer2 = new JoinRequest("BLACK", createdGame.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer2, registeredUser2.authToken()),
                "Player joined black when black was taken");
        JoinRequest newPlayer2Retry = new JoinRequest("WHITE", createdGame.gameID());
        gameService.join(newPlayer2Retry, registeredUser2.authToken());
        JoinRequest newPlayer3 = new JoinRequest("WHITE", createdGame.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer3, registeredUser3.authToken()),
                "Player joined full game");


        CreateRequest newGame2 = new CreateRequest("White taken");
        CreateResult createdGame2 = gameService.create(newGame2, registeredUser.authToken());

        JoinRequest newPlayer5 = new JoinRequest("WHITE", createdGame2.gameID());
        gameService.join(newPlayer5, registeredUser.authToken());
        JoinRequest newPlayer6 = new JoinRequest("WHITE", createdGame2.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer6, registeredUser2.authToken()),
                "Player joined white when white was taken");

        JoinRequest newPlayer7 = new JoinRequest("BLACK", 3);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.join(newPlayer7, registeredUser2.authToken()),
                "Player joined non-existent game");
    }

    @Test
    @DisplayName("Positive List Result")
    public void positiveList() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        ArrayList<GameData> expectedGameList = new ArrayList<>();

        CreateRequest newGame = new CreateRequest("Sally's game 1");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());
        expectedGameList.add(gameService.getGameDAO().getGame(createdGame.gameID()));

        CreateRequest newGame2 = new CreateRequest("Sally's game 2");
        CreateResult createdGame2 = gameService.create(newGame2, registeredUser.authToken());
        expectedGameList.add(gameService.getGameDAO().getGame(createdGame2.gameID()));

        CreateRequest newGame3 = new CreateRequest("Sally's game 3");
        CreateResult createdGame3 = gameService.create(newGame3, registeredUser.authToken());
        expectedGameList.add(gameService.getGameDAO().getGame(createdGame3.gameID()));

        ListResult actualGameList = gameService.list(registeredUser.authToken());

        Assertions.assertEquals(expectedGameList, actualGameList.games(),
                "Returned game list does not match expected");

    }

    @Test
    @DisplayName("Negative List Result")
    public void negativeList() throws DataAccessException {
        UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
        GameService gameService = new GameService(new MemoryGameDAO(), userService.getAuthTokenDAO(), userService.getUserDAO());

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game 1");
        gameService.create(newGame, registeredUser.authToken());

        CreateRequest newGame2 = new CreateRequest("Sally's game 2");
        gameService.create(newGame2, registeredUser.authToken());

        CreateRequest newGame3 = new CreateRequest("Sally's game 3");
        gameService.create(newGame3, registeredUser.authToken());

        LogoutRequest logoutUser = new LogoutRequest();
        userService.logout(registeredUser.authToken());

        Assertions.assertThrows(DataAccessException.class,
                () -> gameService.list(registeredUser.authToken()),
                "User with no auth listedGames");

    }
}