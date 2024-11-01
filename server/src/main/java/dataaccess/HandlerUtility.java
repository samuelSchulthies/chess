package dataaccess;

import handler.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class HandlerUtility {

    private final UserService USER_SERVICE;
    private git cofinal GameService GAME_SERVICE;
    private final ClearService CLEAR_SERVICE;

    public HandlerUtility(UserService USER_SERVICE, GameService GAME_SERVICE, ClearService CLEAR_SERVICE) {
        this.USER_SERVICE = USER_SERVICE;
        this.GAME_SERVICE = GAME_SERVICE;
        this.CLEAR_SERVICE = CLEAR_SERVICE;
    }

    public static final ClearHandler CLEAR_HANDLER = new ClearHandler(CLEAR_SERVICE);
    public static final CreateHandler CREATE_HANDLER = new CreateHandler(GAME_SERVICE);
    public static final JoinHandler JOIN_HANDLER = new JoinHandler(GAME_SERVICE);
    public static final ListHandler LIST_HANDLER = new ListHandler(GAME_SERVICE);
    public static final LoginHandler LOGIN_HANDLER = new LoginHandler(USER_SERVICE);
    public static final LogoutHandler LOGOUT_HANDLER = new LogoutHandler(USER_SERVICE);
    public static final RegisterHandler REGISTER_HANDLER = new RegisterHandler(USER_SERVICE);
    public static final ExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();
}
