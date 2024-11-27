package client.websocket;

import client.GameClient;
import com.google.gson.Gson;
import exception.DataAccessException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;

    ServerMessageHandler serverMessageHandler;

    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message){
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        serverMessageHandler.notify(serverMessage);
                    }
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        GameClient.redraw();
                    }
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){

                    }
                }
            });
        } catch (URISyntaxException | IOException | DeploymentException e){
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void openGameConnection(String authToken, int gameID) throws DataAccessException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID) throws DataAccessException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void closeGameConnection(String authToken, int gameID) throws DataAccessException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
            this.session.close();
        } catch (IOException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws DataAccessException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
