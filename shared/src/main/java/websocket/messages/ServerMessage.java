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
    String message;
    String game;
    String errorMessage;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public void setMessage(String newMessage){
        message = newMessage;
    }
    public void setLoadGame(String newMessage){
        game = newMessage;
    }
    public void setErrorMessage(String newMessage){
        errorMessage = newMessage;
    }

    public String getMessage(){
        return message;
    }
    public String getLoadGame(){
        return game;
    }
    public String getErrorMessage(){
        return errorMessage;
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
