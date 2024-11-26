package repl;

import client.GameClient;
import client.PostLoginClient;
import client.websocket.ServerMessageObserver;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class GameRepl implements ServerMessageObserver {

    private final GameClient gameClient;
    public GameRepl(GameClient gameClient) {
        this.gameClient = gameClient;
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
    private void prompt() {
        System.out.print("\n[IN_GAME] >>> ");
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getServerMessageString() + RESET_TEXT_COLOR);
    }
}
