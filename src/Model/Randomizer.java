package Model;

public class Randomizer {

    /**
     * It gives back a random number in an interval from 0 to val (excluded)
     * @param val - the limit of the interval
     * @return a random number
     */
    public int randomize(int val) {
        int n = (int) (Math.random() * val);
        return n;
    }
}
