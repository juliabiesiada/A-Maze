package Model;

public class Card {
    private Position position;
    private CardType type;
    private int[][] cardMatrix;
    private Rotation rotation;
    private OnCard onCard;
    private boolean available;

    public OnCard getOnCard() {
        return onCard;
    }

    public void setOnCard(OnCard onCard) {
        this.onCard = onCard;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public int[][] getCardMatrix() {
        return cardMatrix;
    }

    public void setCardMatrix(int[][] cardMatrix) {
        this.cardMatrix = cardMatrix;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Card(Position position, CardType type, Rotation rotation) {
        this.position = position;
        this.type = type;
        this.rotation = rotation;
        this.onCard = OnCard.NOTHING;
        this.available = true;

        //Matrixs of all the type of cards we have:
        int[][] tPathMatrix = {
                {4, 1, 2},
                {0, 0, 0},
                {6, 0, 7}
        };
        int[][] linePathMatrix = {
                {1, 3, 6},
                {0, 0, 0},
                {8, 5, 4}
        };
        int[][] anglePathMatrix = {
                {4, 0, 1},
                {7, 0, 0},
                {6, 5, 8}
        };
        int[][] crossPathMatrix = {
                {3, 0, 2},
                {0, 0, 0},
                {4, 0, 7}
        };

        //This fullfill the matrix based on the type of the card:
        switch (type) {
            case TPATH:
                this.cardMatrix = tPathMatrix;
                break;
            case LINEPATH:
                this.cardMatrix = linePathMatrix;
                break;
            case ANGLEPATH:
                this.cardMatrix = anglePathMatrix;
                break;
            case CROSSPATH:
                this.cardMatrix = crossPathMatrix;
                break;
        }
    }

}
