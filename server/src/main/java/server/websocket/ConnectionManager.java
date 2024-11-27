package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username){
        connections.remove(username);
    }

    public void broadcast(ServerMessage message) throws IOException {
        for (var connection : connections.values()){
            if (connection.session.isOpen()){
                connection.send(message.toString());
            }
        }
    }
}
