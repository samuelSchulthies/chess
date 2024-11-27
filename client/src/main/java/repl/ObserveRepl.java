package repl;

import client.ObserveClient;
import client.websocket.ServerMessageHandler;
import websocket.messages.ServerMessage;
import static ui.EscapeSequences.*;

import java.util.Scanner;

public class ObserveRepl implements ServerMessageHandler {

    ObserveClient observeClient;
    public ObserveRepl() {
        observeClient = new ObserveClient();
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

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getServerMessage() + RESET_TEXT_COLOR);
    }

    @Override
    public void loadGame(ServerMessage serverMessage) {

    }

    @Override
    public void errorMessage(ServerMessage serverMessage) {

    }
}
