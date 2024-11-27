package repl;

import client.*;
import client.websocket.ServerMessageHandler;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

import java.util.Scanner;

public class PreLoginRepl {
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;

    public PreLoginRepl(String serverUrl) {
        serverMessageHandler = new ServerMessageHandler();
        server = new ServerFacade(serverUrl);
        postLoginClient = new PostLoginClient(server, serverUrl, serverMessageHandler);
        preLoginClient = new PreLoginClient(server, postLoginClient);
    }

    public void run(){
        System.out.println("Welcome to 240 chess. Type help to get started.\n");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            prompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);
                System.out.print(result);
                if (postLoginClient.getStatus() == UserStatus.SIGNED_IN){
                    PostLoginRepl postLoginRepl = new PostLoginRepl(postLoginClient, server, serverMessageHandler);
                    postLoginRepl.run();
                    postLoginClient.setStatus(UserStatus.SIGNED_OUT);
                }

            } catch (Throwable e){
                System.out.print(e);
            }
        }
        System.out.println();
    }
    public static void prompt() {
        System.out.print("\n[LOGGED_OUT] >>> ");
    }

}
