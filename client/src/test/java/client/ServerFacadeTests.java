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
    private static RegisterRequest newUser;

    @BeforeAll
    public static void init() throws DataAccessException{

        newUser = new RegisterRequest("sally","123","sallyisawesome@gmail.com");

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
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Improper Logout Result")
    public void negativeLogout() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Positive Create Result")
    public void positiveCreate() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Negative Create Result")
    public void negativeCreate() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Positive Join Result")
    public void positiveJoin() throws DataAccessException {
        serverFacade.clear();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Negative Join Result")
    public void negativeJoin() throws DataAccessException {
        serverFacade.clear();
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
