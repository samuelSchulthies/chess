package client;

import exception.DataAccessException;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;
    private final PostLoginClient postLoginClient;

    public PreLoginClient(ServerFacade server, PostLoginClient postLoginClient) {
        this.server = server;
        this.postLoginClient = postLoginClient;
    }

    public String eval(String input){
        try {
            var tokensFromUser = input.toLowerCase().split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0] : "help";
            var params = Arrays.copyOfRange(tokensFromUser, 1, tokensFromUser.length);

            return switch (cmd){
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException e){
            return e.getMessage();
        }
    }

    public String register(String... params) throws DataAccessException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];

            RegisterRequest registerRequest = new RegisterRequest(username, password, email);

            RegisterResult registerResult = server.register(registerRequest);
            postLoginClient.setStatus(UserStatus.SIGNED_IN);

            postLoginClient.setAuthToken(registerResult.authToken());

            return String.format("You have registered and signed in as: " + registerResult.username() + "\n");
        }
        throw new DataAccessException("Incorrect number of register arguments. Please try again\n");
    }

    public String login(String... params) throws DataAccessException {
        if (params.length == 2){
            var username = params[0];
            var password = params[1];

            LoginRequest loginRequest = new LoginRequest(username, password);

            LoginResult loginResult = server.login(loginRequest);
            postLoginClient.setStatus(UserStatus.SIGNED_IN);

            postLoginClient.setAuthToken(loginResult.authToken());

            return String.format("Welcome back " + loginResult.username() + "\n");
        }
        throw new DataAccessException("Incorrect number of login arguments. Please try again\n");
    }
    public String help(){
        return """
                
                register <USERNAME> <PASSWORD> <EMAIL>
                login <USERNAME> <PASSWORD>
                help (displays commands again)
                quit
                """;
    }

}
