package client.websocket;

import client.GameClient;
import client.GameInfo;
import client.PostLoginClient;
import client.PromptSwitcher;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class ServerMessageHandler {

    public ServerMessageHandler(){

    }
    void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getServerMessage() + RESET_TEXT_COLOR);
        PromptSwitcher promptSwitcher = new PromptSwitcher();
        promptSwitcher.runPrompt();
    }

    void loadGame(ServerMessage serverMessage) {
    }

    void errorMessage(ServerMessage serverMessage) {

    }
}
