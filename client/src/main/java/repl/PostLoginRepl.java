package repl;

import client.GameClient;
import client.PostLoginClient;
import client.UserStatus;
import client.websocket.ServerMessageHandler;
import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class PostLoginRepl {
    private final PostLoginClient postLoginClient;
    private final ServerFacade server;
    private final ServerMessageHandler serverMessageHandler;
    public PostLoginRepl(PostLoginClient postLoginClient, ServerFacade server, ServerMessageHandler serverMessageHandler) {
        this.postLoginClient = postLoginClient;
        this.server = server;
        this.serverMessageHandler = serverMessageHandler;
//        gameClient = new GameClient(server);
    }

    public void run(){
        System.out.println("Type help to see options.\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("logout")) {
            prompt();
            String line = scanner.nextLine();

            try {
                result = postLoginClient.eval(line);
                System.out.print(result);
                if (postLoginClient.getStatus() == UserStatus.IN_GAME){
                    GameRepl gameRepl = new GameRepl(server, serverMessageHandler,
                            postLoginClient.getWs(), postLoginClient.getGameInfo());
                    gameRepl.run();
                    postLoginClient.setStatus(UserStatus.SIGNED_IN);
                }
                if (postLoginClient.getStatus() == UserStatus.OBSERVING){
                    ObserveRepl observeRepl = new ObserveRepl();
                    observeRepl.run();
                    postLoginClient.setStatus(UserStatus.SIGNED_IN);
                }

            } catch (Throwable e){
                System.out.print(e);
            }
        }
        System.out.println();
    }
    public static void prompt() {
        System.out.print("\n[LOGGED_IN] >>> ");
    }

}
