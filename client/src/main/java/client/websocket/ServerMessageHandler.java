package client.websocket;

import websocket.messages.ServerMessage;
public interface ServerMessageHandler {
    void notify(ServerMessage serverMessage);
    void loadGame(ServerMessage serverMessage);
    void errorMessage(ServerMessage serverMessage);
}
