package client;

import dataaccess.DataAccessException;
import dataaccess.MySQLAuthTokenDAO;
import dataaccess.MySQLGameDAO;
import dataaccess.MySQLUserDAO;
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

    @BeforeAll
    public static void init() {
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
        UserService userService = new UserService(new MySQLUserDAO(), new MySQLAuthTokenDAO());
        GameService gameService = new GameService(new MySQLGameDAO(), userService.getAuthTokenDAO());
        ClearService clearService = new ClearService(userService, gameService);

        clearService.clear();

        RegisterRequest newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");
        RegisterResult registerResult = serverFacade.register(newUser);

        Assertions.assertEquals(newUser.username(), registerResult.username(),
                "Username of registered user does not match database");
        Assertions.assertNotNull(registerResult.authToken(), "Registered user did not return authToken");

        clearService.clear();
    }

    @Test
    @DisplayName("Improper Register Result")
    public void negativeRegister() throws DataAccessException {
    }

    @Test
    @DisplayName("Proper Login Result")
    public void positiveLogin() throws DataAccessException {
    }

    @Test
    @DisplayName("Improper Login Result")
    public void negativeLogin() throws DataAccessException {

    }

    @Test
    @DisplayName("Proper Logout Result")
    public void positiveLogout() throws DataAccessException {

    }

    @Test
    @DisplayName("Improper Logout Result")
    public void negativeLogout() throws DataAccessException {

    }

    @Test
    @DisplayName("Positive Create Result")
    public void positiveCreate() throws DataAccessException {
    }

    @Test
    @DisplayName("Negative Create Result")
    public void negativeCreate() throws DataAccessException {
    }

    @Test
    @DisplayName("Positive Join Result")
    public void positiveJoin() throws DataAccessException {
    }

    @Test
    @DisplayName("Negative Join Result")
    public void negativeJoin() throws DataAccessException {
    }

    @Test
    @DisplayName("Positive List Result")
    public void positiveList() throws DataAccessException {

    }

    @Test
    @DisplayName("Negative List Result")
    public void negativeList() throws DataAccessException {

    }

    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException {

    }

}
