package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> gameIDToUserSession = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID) {
        var connection = new Connection(username, session);
        gameIDToUserSession.putIfAbsent(gameID, new ArrayList<>());
        gameIDToUserSession.get(gameID).add(connection);
    }

    public void remove(String username, int gameID){
        gameIDToUserSession.get(gameID).removeIf(connection -> connection.username.equals(username));
    }

    public void broadcastOne(ServerMessage message, String username, int gameID) throws IOException {
        for (var connectionsFromID : gameIDToUserSession.get(gameID))
            if(Objects.equals(connectionsFromID.username, username)){
                connectionsFromID.send(message.toString());
            }
    }

    public void broadcastAll(ServerMessage message, int gameID) throws IOException {
        for (var connectionsFromID : gameIDToUserSession.get(gameID))
            connectionsFromID.send(message.toString());
    }
}
