package client;

import repl.GameRepl;
import repl.ObserveRepl;
import repl.PostLoginRepl;
import repl.PreLoginRepl;

public class PromptSwitcher {

    public void runPrompt(){
        if (PostLoginClient.status == UserStatus.SIGNED_IN){
            PostLoginRepl.prompt();
        }
        if (PostLoginClient.status == UserStatus.SIGNED_OUT){
            PreLoginRepl.prompt();
        }
        if (PostLoginClient.status == UserStatus.IN_GAME){
            GameRepl.prompt();
        }
        if (PostLoginClient.status == UserStatus.OBSERVING){
            ObserveRepl.prompt();
        }
    }

}
