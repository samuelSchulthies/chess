package client.websocket;

import client.*;
import exception.DataAccessException;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

public class ServerMessageHandler {

    GameClient gameClient;
    ObserveClient observeClient;

    PromptSwitcher promptSwitcher = new PromptSwitcher();
    public ServerMessageHandler(){

    }
    void notify(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_BLUE + serverMessage.getMessage() + RESET_TEXT_COLOR);
        promptSwitcher.runPrompt();
    }

    void loadGame(ServerMessage serverMessage) throws DataAccessException {
        if (gameClient != null){
            gameClient.redraw();
        }
        if (observeClient != null){
            observeClient.redraw();
        }
        promptSwitcher.runPrompt();
    }

    void errorMessage(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getErrorMessage() + RESET_TEXT_COLOR);
        promptSwitcher.runPrompt();
    }

    public void setGameClient(GameClient initializedGameClient){
        gameClient = initializedGameClient;
    }

    public void setObserveClient(ObserveClient initializedObserveClient){
        observeClient = initializedObserveClient;
    }
}
