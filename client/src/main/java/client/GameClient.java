package client;

import server.ServerFacade;

public class GameClient {
    private final ServerFacade server;

    public GameClient(ServerFacade server) {
        this.server = server;
    }
}
