package repl;

import client.GameInfo;
import client.ObserveClient;
import client.PostLoginClient;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;

import java.util.Scanner;

public class ObserveRepl{

    ObserveClient observeClient;
    ServerMessageHandler serverMessageHandler;
    WebSocketFacade ws;
    GameInfo gameInfo;
    PostLoginClient postLoginClient;

    public ObserveRepl(ServerMessageHandler serverMessageHandler,
                       WebSocketFacade ws, GameInfo gameInfo, PostLoginClient postLoginClient) {
        this.serverMessageHandler = serverMessageHandler;
        this.ws = ws;
        this.gameInfo = gameInfo;
        this.postLoginClient = postLoginClient;
        observeClient = new ObserveClient(postLoginClient, gameInfo, ws);
    }

    public void run(){
        System.out.println("You are now observing. Type help to see commands.\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("leave")) {
            prompt();
            String line = scanner.nextLine();

            try {
                result = observeClient.eval(line);
                System.out.print(result);

            } catch (Throwable e){
                System.out.print(e);
            }
        }
        System.out.println();
    }
    public static void prompt() {
        System.out.print("\n[OBSERVING] >>> ");
    }
}
