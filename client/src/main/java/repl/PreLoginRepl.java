package repl;

import client.GameClient;
import client.PostLoginClient;
import client.PreLoginClient;
import client.UserStatus;
import server.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.Scanner;

public class PreLoginRepl {

    private final GameClient gameClient;
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;

    public PreLoginRepl(String serverUrl) {
        ServerFacade server = new ServerFacade(serverUrl);
        gameClient = new GameClient(server);
        postLoginClient = new PostLoginClient(server);
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
                    PostLoginRepl postLoginRepl = new PostLoginRepl(postLoginClient);
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

}
