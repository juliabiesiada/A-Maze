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

    public void updateBuffDebuff() {
        int numOfBuff = buffPositions.size();
        int numOfDebuff = debuffPositions.size();
        while (numOfBuff < 2 || numOfDebuff < 2) {
            int row = new Randomizer().randomize(cardsOnBoard.length);
            int col = new Randomizer().randomize(cardsOnBoard.length);
            if (cardsOnBoard[row][col].isAvailable() == true) {
                if (cardsOnBoard[row][col].getOnCard() == OnCard.NOTHING) {
                    if (numOfBuff <= 1) {
                        cardsOnBoard[row][col].setOnCard(OnCard.BUFFER);
                        numOfBuff++;
                        buffPositions.add(new Position(row, col));
                    } else if (numOfDebuff <= 1) {
                        cardsOnBoard[row][col].setOnCard(OnCard.DEBUFFER);
                        numOfDebuff++;
                        debuffPositions.add(new Position(row, col));
                    }
                }
            }
        }
    }

    public void removeBuffDebuff(Position pos) {
        if (cardsOnBoard[pos.getRow()][pos.getColumn()].getOnCard() == OnCard.BUFFER) {
            cardsOnBoard[pos.getRow()][pos.getColumn()].setOnCard(OnCard.NOTHING);
            for (int i = 0; i<buffPositions.size(); i++) {
                if (buffPositions.get(i).getColumn() == pos.getColumn() && buffPositions.get(i).getRow() == pos.getRow()) {
                    buffPositions.remove(i);
                }
            }
        }
        else if (cardsOnBoard[pos.getRow()][pos.getColumn()].getOnCard() == OnCard.DEBUFFER) {
            cardsOnBoard[pos.getRow()][pos.getColumn()].setOnCard(OnCard.NOTHING);
            for (int i = 0; i<debuffPositions.size(); i++) {
                if (debuffPositions.get(i).getColumn() == pos.getColumn() && debuffPositions.get(i).getRow() == pos.getRow()) {
                    debuffPositions.remove(i);
                }
            }
        }
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
