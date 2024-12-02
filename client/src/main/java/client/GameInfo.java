package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import service.GameService;

public class GameInfo {

    private final String authToken;
    private final int gameID;
    private final String team;
    private final ChessGame game;
    private ChessBoard board;
    public boolean waitingForPlayer = true;

    public GameInfo(String authToken, int gameID, String team, ChessGame game){
        this.authToken = authToken;
        this.gameID = gameID;
        this.team = team;
        this.game = game;
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

    public ChessGame getGame(){
        return game;
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
