package Model;

import java.util.ArrayList;

public class Game {
    private Player[] players;
    private Board board;
    private Turn[] turns;
    private Level level;

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Turn[] getTurns() {
        return turns;
    }

    public void setTurns(Turn[] turns) {
        this.turns = turns;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
