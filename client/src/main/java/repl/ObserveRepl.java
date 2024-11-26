package repl;

import client.ObserveClient;
import client.UserStatus;

import java.util.Scanner;

public class ObserveRepl {

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
    private void prompt() {
        System.out.print("\n[OBSERVING] >>> ");
    }

}
