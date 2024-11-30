package websocket.messages;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String serverMessage;
    String game;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public void setServerMessageNotification(String newMessage){
        serverMessage = new NotificationMessage(newMessage).message();
    }
    public void setServerMessageError(String newMessage){
        serverMessage = new ErrorMessage(newMessage).message();
    }
    public void setServerMessageLoadGame(String newMessage){
        serverMessage = new LoadGameMessage(newMessage).game();
    }
    public String getServerMessage(){
        return serverMessage;
    }


    public String toString(){
        return new Gson().toJson(this);
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

//    public String LoadGameMessage(String game){
//        return game;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
