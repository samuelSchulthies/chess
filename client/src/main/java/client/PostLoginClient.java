package client;

import server.ServerFacade;

public class PostLoginClient {
    private final ServerFacade server;

    public PostLoginClient(ServerFacade server) {
        this.server = server;
    }
}

