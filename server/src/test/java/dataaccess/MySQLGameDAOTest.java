package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import exception.DataAccessException;

import java.util.ArrayList;

public class MySQLGameDAOTest {

    @Test
    @DisplayName("Proper Create Game")
    public void positiveCreateGame() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        Assertions.assertNotNull(gameService.getGameDAO().getGame(createdGame.gameID()),
                "returned a non-existent game");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Create Game")
    public void negativeCreateGame() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        CreateRequest newGame = new CreateRequest("Sally's game");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.create(newGame, "guy"),
                "created a game with bad auth");

        clearService.clear();
    }

    @Test
    @DisplayName("Proper Get Game")
    public void positiveGetGame() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        Assertions.assertEquals(newGame.gameName(), gameService.getGameDAO().getGame(createdGame.gameID()).gameName(),
                "did not return expected game");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Get Game")
    public void negativeGetGame() throws DataAccessException {

        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        Assertions.assertNull(gameService.getGameDAO().getGame(1),
                "found game that shouldn't exist");

        clearService.clear();

    }

    @Test
    @DisplayName("Proper List Games")
    public void positiveListGames() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

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

        ListResult actualGameList = gameService.getGameDAO().listGames();

        Assertions.assertEquals(expectedGameList, actualGameList.games(),
                "Returned game list does not match expected");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper List Games")
    public void negativeListGames() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game 1");
        gameService.create(newGame, registeredUser.authToken());

        CreateRequest newGame2 = new CreateRequest("Sally's game 2");
        gameService.create(newGame2, registeredUser.authToken());

        CreateRequest newGame3 = new CreateRequest("Sally's game 3");
        gameService.create(newGame3, registeredUser.authToken());

        userService.logout(registeredUser.authToken());

        Assertions.assertThrows(DataAccessException.class,
                () -> gameService.list(registeredUser.authToken()),
                "User with no auth listedGames");

        clearService.clear();
    }

    @Test
    @DisplayName("Proper Update Game")
    public void positiveUpdateGame() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID());
        gameService.join(newPlayer, registeredUser.authToken());

        Assertions.assertEquals("sally",
                gameService.getGameDAO().getGame(createdGame.gameID()).blackUsername(),
                "User selected black but game was not updated");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Update Game")
    public void negativeUpdateGame() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("david","321","davidisawesome@gmail.com");
        RegisterResult registeredUser2 = userService.register(newUser2);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID());
        gameService.join(newPlayer, registeredUser.authToken());
        JoinRequest newPlayer2 = new JoinRequest("BLACK", createdGame.gameID());
        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.join(newPlayer2, registeredUser2.authToken()),
                "game updated with already taken team");

        clearService.clear();
    }

    @Test
    @DisplayName("Clear Game")
    public void clearGame() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registeredUser = userService.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = gameService.create(newGame, registeredUser.authToken());

        gameService.getGameDAO().clear(gameService);
        Assertions.assertNull(gameService.getGameDAO().getGame(createdGame.gameID()),
                "retrieved non-existent game");

        clearService.clear();
    }

}
