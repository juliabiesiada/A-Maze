package Model;

import java.util.ArrayList;

public class Game {
    private Player[] players;
    private Card[][] cardsOnBoard;
    private TurnManager turnsOrder;
    private Level level;
    private ArrayList<Gem> gems;
    private Status status;
    private ArrayList<Position> buffPositions;
    private ArrayList<Position> debuffPositions;

    public Game() {
        this.gems = new ArrayList<Gem>();
        this.status = new Status();
        this.buffPositions = new ArrayList<Position>();
        this.debuffPositions = new ArrayList<Position>();
    }

    public ArrayList<Position> getBuffPositions() {
        return buffPositions;
    }

    public ArrayList<Position> getDebuffPositions() {
        return debuffPositions;
    }

    public TurnManager getTurnsOrder() {
        return turnsOrder;
    }

    public void setTurnsOrder(TurnManager turnsOrder) {
        this.turnsOrder = turnsOrder;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Card[][] getCardsOnBoard() {
        return cardsOnBoard;
    }

    public void setCardsOnBoard(Card[][] cardsOnBoard) {
        this.cardsOnBoard = cardsOnBoard;
    }

    public ArrayList<Gem> getGems() {
        return gems;
    }

    public void setTurnsOrders(TurnManager turns) {
        this.turnsOrder = turns;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
