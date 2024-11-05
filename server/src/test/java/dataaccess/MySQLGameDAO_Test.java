package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requestresult.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class MySQLGameDAO_Test {

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

    }

    @Test
    @DisplayName("Improper List Games")
    public void negativeListGames() throws DataAccessException {

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

        gameService.getGameDAO().clear();
        Assertions.assertNull(gameService.getGameDAO().getGame(createdGame.gameID()),
                "retrieved non-existent game");

        clearService.clear();
    }

}
