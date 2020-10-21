package Model;

import java.util.ArrayList;

public class TurnManager {

    private Player[] players;
    private ArrayList<String> turns;
    int counter;
    boolean firstPlayer;

    public TurnManager(Player[] players) {
        this.players = players;
        this.turns = new ArrayList<>();
        turnInitializer();
        this.counter = 0;
        this.firstPlayer = false;
    }

    //GETTER
    public int getCounter() {
        return counter;
    }

    /**
     * This method initialize the first 3 turns of the game
     */
    public void turnInitializer() {
        for (int i = 0; i<3; i++) {
            for (Player player : players) {
                turns.add(player.getName());
            }
        }
    }

    /**
     * This method manages the receipt of a buff
     * @param player who receives the buff
     */
    public void bufferReceived(Player player) {
        boolean buffAdded = false;
        for (int i = 0; i<turns.size(); i++) {
            if (turns.get(i).equals(player.getName()) && !buffAdded) {
                turns.add(i+1, player.getName());
                buffAdded = true;
            }
        }
        System.out.println("TurnManager was updated, buff was added.");
    }

    /**
     * This method manages the receipt of a debuff
     * @param player who receives the debuff
     */
    public void debufferReceived(Player player) {
        boolean debuffAdded = false;
        int turnsToRemove = 2;
        for (int i = 0; i<turns.size(); i++) {
            if (turns.get(i).equals(player.getName()) && !debuffAdded) {
                turns.remove(i);
                i--;
                turnsToRemove--;
            } else if (turnsToRemove == 0) {
                debuffAdded = true;
            }
        }
        System.out.println("TurnManager was updated, debuff was added.");
    }

    /**
     * This method gives you back the name of player who has to play
     * @return a string with the name of the player
     */
    public String whoIsNext() {
        return turns.get(0);
    }

    /**
     * This method removes from turns the player who has just ended hi turn and put him in the bottom of the list
     * @param player player who just ended a turn
     */
    public void turnCompleted(Player player) {
        if (turns.get(0).equals(player.getName())) {
            turns.remove(0);
            if (player.getName().equals(players[0].getName())) {
                firstPlayer = true;
            }
            if (firstPlayer) {
                for (Player value : players) {
                    turns.add(value.getName());
                }
                firstPlayer = false;
            }
            counter++;
        }
    }

    public Player whosPlaying() {
        Player currentPlayer = null;
        for(Player player: players) {
            if (player.getName().equals(turns.get(0))){
                currentPlayer = player;
            }
        }
        return currentPlayer;
    }
}
