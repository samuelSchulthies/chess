package repl;

import client.GameClient;
import client.GameInfo;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class GameRepl {

    private final GameClient gameClient;
    public GameRepl(ServerFacade server, ServerMessageHandler serverMessageHandler,
                    WebSocketFacade ws, GameInfo gameInfo) {
        gameClient = new GameClient(server, serverMessageHandler, ws, gameInfo);
    }

    public void run(){
        System.out.println("You have entered a game. Type help to see commands.\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("leave")) {
            prompt();
            String line = scanner.nextLine();

            try {
                result = gameClient.eval(line);
                System.out.print(result);

            } catch (Throwable e){
                System.out.print(e);
            }
        }
        System.out.println();
    }
    public static void prompt() {
        System.out.print("\n[IN_GAME] >>> ");
    }

}
