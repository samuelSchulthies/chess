package repl;

import client.GameClient;
import client.PostLoginClient;

import java.util.Scanner;

public class GameRepl {

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

}
