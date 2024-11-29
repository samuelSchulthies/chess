package client;

import chess.ChessBoard;

public class GameInfo {

    private String authToken;
    private int gameID;
    private String team;
    private ChessBoard board;
    public boolean waitingForPlayer = true;

    public GameInfo(String authToken, int gameID, String team, ChessBoard board){
        this.authToken = authToken;
        this.gameID = gameID;
        this.team = team;
        this.board = board;
    }

    public int getGameID() {
        return gameID;
    }

    public String getTeam() {
        return team;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setBoard(ChessBoard updatedBoard){
        board = updatedBoard;
    }

    public void setWaitingForPlayer(boolean setWaitingForPlayer){
        waitingForPlayer = setWaitingForPlayer;
    }

    public boolean getWaitingForPlayer(){
        return waitingForPlayer;
    }
}
