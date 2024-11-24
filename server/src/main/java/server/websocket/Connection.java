package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {

    public int gameID;
    public Session session;

    public Connection(int gameID, Session session){
        this.gameID = gameID;
        this.session = session;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
