package dataaccess;

import handler.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class MemoryDataAccess {
    private static final UserService USER_SERVICE = new UserService(new MemoryUserDAO(), new MemoryAuthTokenDAO());
    private static final GameService GAME_SERVICE = new GameService(new MemoryGameDAO(), USER_SERVICE.getAuthTokenDAO());
    private static final ClearService CLEAR_SERVICE = new ClearService(USER_SERVICE, GAME_SERVICE);

    public static final ClearHandler CLEAR_HANDLER = new ClearHandler(CLEAR_SERVICE);
    public static final CreateHandler CREATE_HANDLER = new CreateHandler(GAME_SERVICE);
    public static final JoinHandler JOIN_HANDLER = new JoinHandler(GAME_SERVICE);
    public static final ListHandler LIST_HANDLER = new ListHandler(GAME_SERVICE);
    public static final LoginHandler LOGIN_HANDLER = new LoginHandler(USER_SERVICE);
    public static final LogoutHandler LOGOUT_HANDLER = new LogoutHandler(USER_SERVICE);
    public static final RegisterHandler REGISTER_HANDLER = new RegisterHandler(USER_SERVICE);
    public static final ExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();
}
