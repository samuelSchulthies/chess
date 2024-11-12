package client;

import dataaccess.*;
import org.junit.jupiter.api.*;
import requestresult.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;
import service.GameService;
import service.UserService;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static RegisterRequest newUser;

    private static CreateRequest newGame;

    @BeforeAll
    public static void init() throws DataAccessException{

        newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        newGame = new CreateRequest("Sally's game");

        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Proper Register Result")
    public void positiveRegister() throws DataAccessException {

        serverFacade.clear();

        RegisterResult registerResult = serverFacade.register(newUser);

        Assertions.assertEquals(newUser.username(), registerResult.username(),
                "Username of registered user does not match database");
        Assertions.assertNotNull(registerResult.authToken(), "Registered user did not return authToken");

        serverFacade.clear();
    }

    @Test
    @DisplayName("Improper Register Result")
    public void negativeRegister() throws DataAccessException {
        serverFacade.clear();

        serverFacade.register(newUser);
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.register(newUser));

        RegisterRequest newUserNameNull = new RegisterRequest("",null,"sallyisnotawesome@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.register(newUserNameNull));

        serverFacade.clear();
    }

    @Test
    @DisplayName("Proper Login Result")
    public void positiveLogin() throws DataAccessException {
        serverFacade.clear();
        
        RegisterResult registeredUser = serverFacade.register(newUser);
        serverFacade.logout(registeredUser.authToken());
        
        LoginRequest loginUser = new LoginRequest("sally", "123");
        LoginResult loggedInUser = serverFacade.login(loginUser);
        String authTokenStorage = loggedInUser.authToken();

        Assertions.assertNotNull(registeredUser.authToken(),
                "User logged in but authToken is not found");
        
        serverFacade.clear();
    }

    @Test
    @DisplayName("Improper Login Result")
    public void negativeLogin() throws DataAccessException {
        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);

        serverFacade.logout(registeredUser.authToken());

        LoginRequest loginUserBadUsername = new LoginRequest("saly", "123");
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.login(loginUserBadUsername));

        LoginRequest loginUserBadPassword = new LoginRequest("sally", "1234");
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.login(loginUserBadPassword));

        serverFacade.clear();
    }

    @Test
    @DisplayName("Proper Logout Result")
    public void positiveLogout() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);
        serverFacade.logout(registeredUser.authToken());


        Assertions.assertNull(userService.getAuthTokenDAO().getAuth(registeredUser.authToken()),
                "AuthToken was not removed");

        serverFacade.clear();
    }

    @Test
    @DisplayName("Improper Logout Result")
    public void negativeLogout() throws DataAccessException {
        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);

        serverFacade.logout(registeredUser.authToken());

        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.logout(registeredUser.authToken()),
                "User logged out with bad auth");

        serverFacade.clear();
    }

    @Test
    @DisplayName("Positive Create Result")
    public void positiveCreate() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());

        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = serverFacade.create(newGame, registeredUser.authToken());

        Assertions.assertNotNull(gameService.getGameDAO().getGame(createdGame.gameID()),
                "Game with given gameID was not found");

        serverFacade.clear();
    }

    @Test
    @DisplayName("Negative Create Result")
    public void negativeCreate() throws DataAccessException {
        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);

        CreateRequest newGameEmptyName = new CreateRequest("");
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.create(newGameEmptyName, registeredUser.authToken()));

        serverFacade.logout(registeredUser.authToken());

        CreateRequest newGameNoAuth = new CreateRequest("Sally's game");
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.create(newGameNoAuth, registeredUser.authToken()));

        serverFacade.clear();
    }

    @Test
    @DisplayName("Positive Join Result")
    public void positiveJoin() throws DataAccessException {
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());

        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("benson","321","benisawesome@gmail.com");
        RegisterResult registeredUser2 = serverFacade.register(newUser2);

        CreateRequest newGame = new CreateRequest("Sally's game");
        CreateResult createdGame = serverFacade.create(newGame, registeredUser.authToken());

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID());
        serverFacade.join(newPlayer, registeredUser.authToken());
        JoinRequest newPlayer2 = new JoinRequest("WHITE", createdGame.gameID());
        serverFacade.join(newPlayer2, registeredUser2.authToken());

        Assertions.assertEquals(registeredUser.username(),
                gameService.getGameDAO().getGame(createdGame.gameID()).blackUsername(),
                "Joined player did not take BLACK's spot");

        Assertions.assertEquals(registeredUser2.username(),
                gameService.getGameDAO().getGame(createdGame.gameID()).whiteUsername(),
                "Joined player did not take WHITE's spot");

        serverFacade.clear();
    }

    @Test
    @DisplayName("Negative Join Result")
    public void negativeJoin() throws DataAccessException {
        serverFacade.clear();

        RegisterResult registeredUser = serverFacade.register(newUser);
        RegisterRequest newUser2 = new RegisterRequest("benson","321","benisawesome@gmail.com");
        RegisterResult registeredUser2 = serverFacade.register(newUser2);
        RegisterRequest newUser3 = new RegisterRequest("jason","312","jasonisawesome@gmail.com");
        RegisterResult registeredUser3 = serverFacade.register(newUser3);

        CreateRequest newGame = new CreateRequest("Black taken and full game");
        CreateResult createdGame = serverFacade.create(newGame, registeredUser.authToken());

        JoinRequest newPlayer = new JoinRequest("BLACK", createdGame.gameID());
        serverFacade.join(newPlayer, registeredUser.authToken());
        JoinRequest newPlayer2 = new JoinRequest("BLACK", createdGame.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.join(newPlayer2, registeredUser2.authToken()),
                "Player joined black when black was taken");
        JoinRequest newPlayer2Retry = new JoinRequest("WHITE", createdGame.gameID());
        serverFacade.join(newPlayer2Retry, registeredUser2.authToken());
        JoinRequest newPlayer3 = new JoinRequest("WHITE", createdGame.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.join(newPlayer3, registeredUser3.authToken()),
                "Player joined full game");


        CreateRequest newGame2 = new CreateRequest("White taken");
        CreateResult createdGame2 = serverFacade.create(newGame2, registeredUser.authToken());

        JoinRequest newPlayer5 = new JoinRequest("WHITE", createdGame2.gameID());
        serverFacade.join(newPlayer5, registeredUser.authToken());
        JoinRequest newPlayer6 = new JoinRequest("WHITE", createdGame2.gameID());
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.join(newPlayer6, registeredUser2.authToken()),
                "Player joined white when white was taken");

        JoinRequest newPlayer7 = new JoinRequest("BLACK", 3);
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.join(newPlayer7, registeredUser2.authToken()),
                "Player joined non-existent game");

        serverFacade.clear();
    }

    @Test
    @DisplayName("Positive List Result")
    public void positiveList() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Negative List Result")
    public void negativeList() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

}
