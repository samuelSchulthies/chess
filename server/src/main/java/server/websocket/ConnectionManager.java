package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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

    public void broadcast(){

    }
}
