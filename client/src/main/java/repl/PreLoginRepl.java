package repl;

import client.GameClient;
import client.PostLoginClient;
import client.PreLoginClient;
import client.UserStatus;
import client.websocket.ServerMessageHandler;
import server.Server;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

import java.util.Scanner;

public class PreLoginRepl implements ServerMessageHandler {
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final ServerFacade server;

    public PreLoginRepl(String serverUrl) {
        server = new ServerFacade(serverUrl);
        postLoginClient = new PostLoginClient(server, serverUrl, this);
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
                    PostLoginRepl postLoginRepl = new PostLoginRepl(postLoginClient, server, this);
                    postLoginRepl.run();
                    postLoginClient.setStatus(UserStatus.SIGNED_OUT);
                }

            } catch (Throwable e){
                System.out.print(e);
            }
        }
        System.out.println();
    }
    private void prompt() {
        System.out.print("\n[LOGGED_OUT] >>> ");
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getServerMessageString() + RESET_TEXT_COLOR);
    }

}
