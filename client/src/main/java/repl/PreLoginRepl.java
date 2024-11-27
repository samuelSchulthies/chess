package repl;

import client.*;
import client.websocket.ServerMessageHandler;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

import java.util.Scanner;

public class PreLoginRepl implements ServerMessageHandler {
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final ServerFacade server;
    private final PromptSwitcher promptSwitcher;

    public PreLoginRepl(String serverUrl) {
        server = new ServerFacade(serverUrl);
        postLoginClient = new PostLoginClient(server, serverUrl, this);
        preLoginClient = new PreLoginClient(server, postLoginClient);
        promptSwitcher = new PromptSwitcher();
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
    public static void prompt() {
        System.out.print("\n[LOGGED_OUT] >>> ");
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getServerMessage() + RESET_TEXT_COLOR);
        promptSwitcher.runPrompt();
    }

    @Override
    public void loadGame(ServerMessage serverMessage) {

    }

    @Override
    public void errorMessage(ServerMessage serverMessage) {

    }

}
