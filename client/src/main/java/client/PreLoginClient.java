package client;

import dataaccess.DataAccessException;
import requestresult.RegisterRequest;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String input){
        try {
            var tokensFromUser = input.toLowerCase().split(" ");
            var cmd = (tokensFromUser.length > 0) ? tokensFromUser[0] : "help";
            var params = Arrays.copyOfRange(tokensFromUser, 1, tokensFromUser.length);

            return switch (cmd){
                case "register" -> register(params);
                case "login" -> login(params);
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

            return String.format("You have registered and signed in as: " + server.register(registerRequest));
        }
        throw new DataAccessException("One of the register fields is blank");
    }

    public String login(String... params) throws DataAccessException {
        return null;
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
