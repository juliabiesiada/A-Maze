package Model;

import java.util.ArrayList;

public class TurnManager {

    private Player[] players;
    private ArrayList<String> turns;

    public TurnManager(Player[] players) {
        this.players = players;
        this.turns = new ArrayList<>();
        turnInitializer();
    }

    //This is to force the pull <3

    /**
     * This method initialize the first 3 turns of the game
     */
    public void turnInitializer() {
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<players.length; j++) {
                turns.add(players[j].getName());
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
            if (turns.get(i) == player.getName() && buffAdded == false) {
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
            if (turns.get(i) == player.getName() && debuffAdded == false) {
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
     * @param player
     */
    public void turnCompleted(Player player) {
        if (turns.get(0) == player.getName()) {
            turns.remove(0);
            turns.add(player.getName());
        }
    }

}
