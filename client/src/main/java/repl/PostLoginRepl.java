package repl;

import client.PostLoginClient;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class PostLoginRepl {
    private final PostLoginClient postLoginClient;
    public PostLoginRepl(PostLoginClient postLoginClient) {
        this.postLoginClient = postLoginClient;
    }

    public void run(){
        System.out.println("Type help to see game commands.\n");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("logout")) {
            prompt();
            String line = scanner.nextLine();

            try {
                result = postLoginClient.eval(line);
                System.out.print(result);

            } catch (Throwable e){
                System.out.print(e);
            }
        }
        System.out.println();
    }
    private void prompt() {
        System.out.print("\n[LOGGED_IN] >>> ");
    }

}
