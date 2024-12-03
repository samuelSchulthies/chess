package repl;

import client.GameClient;
import client.GameInfo;
import client.PostLoginClient;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import server.ServerFacade;

import java.util.Scanner;

public class GameRepl {

    private final GameClient gameClient;
    public GameRepl(ServerFacade server, ServerMessageHandler serverMessageHandler,
                    WebSocketFacade ws, GameInfo gameInfo, PostLoginClient postLoginClient) {
        gameClient = new GameClient(server, serverMessageHandler, ws, gameInfo, postLoginClient);
        serverMessageHandler.setGameClient(gameClient);
    }

    public void run(){

        Scanner scanner = new Scanner(System.in);
        var result = "";

        System.out.println("Type help to see commands.\n");
        while (!result.equals("leave")) {
            prompt();
            String line = scanner.nextLine();

            try {
                result = gameClient.eval(line);
                System.out.print(result);

                if (result.equals("resign")){
                    gameClient.setResignRestrictionFlag(true);
                }

            } catch (Throwable e){
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }
    public static void prompt() {
        System.out.print("\n[IN_GAME] >>> ");
    }

}
