package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        var connection = new Connection(gameID, session);
        connections.put(gameID, connection);
    }

    public void remove(int gameID){
        connections.remove(gameID);
    }

    public void broadcast(ServerMessage notification) throws IOException {
        for (var connection : connections.values()){
            if (connection.session.isOpen()){
                connection.send(notification.toString());
            }
        }
    }
}
