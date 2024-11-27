package client.websocket;

import client.GameClient;
import client.GameInfo;
import client.PostLoginClient;
import client.PromptSwitcher;
import exception.DataAccessException;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class ServerMessageHandler {

    GameClient gameClient;

    PromptSwitcher promptSwitcher = new PromptSwitcher();
    public ServerMessageHandler(){

    }
    void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getServerMessage() + RESET_TEXT_COLOR);
        promptSwitcher.runPrompt();
    }

    void loadGame(ServerMessage serverMessage) throws DataAccessException {
        gameClient.redraw();
        promptSwitcher.runPrompt();
    }

    void errorMessage(ServerMessage serverMessage) {
    }

    public void setGameClient(GameClient initializedGameClient){
        gameClient = initializedGameClient;
    }
}
