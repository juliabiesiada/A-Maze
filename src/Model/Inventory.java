package Model;

public class Inventory {
    private int gemsCollected;
    private int bufferCollected;
    private int debufferCollected;

    public Inventory() {
        this.gemsCollected = 0;
        this.bufferCollected = 0;
        this.debufferCollected = 0;
    }

    public int getGemsCollected() {
        return gemsCollected;
    }

    public void setGemsCollected(int gemsCollected) {
        this.gemsCollected = gemsCollected;
    }

    public int getBufferCollected() {
        return bufferCollected;
    }

    public void setBufferCollected(int bufferCollected) {
        this.bufferCollected = bufferCollected;
    }

    public int getDebufferCollected() {
        return debufferCollected;
    }

    public void setDebufferCollected(int debufferCollected) {
        this.debufferCollected = debufferCollected;
    }
}