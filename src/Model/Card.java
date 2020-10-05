package Model;

public class Card {
    private int idNumber;
    private int row;
    private int column;
    private CardType type;
    private int[][] cardMatrix;
    private Rotation rotation;

    public Rotation getRotation() {
        return rotation;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
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

    public Card(int idNumber, int row, int column, CardType type, Rotation rotation) {
        this.idNumber = idNumber;
        this.row = row;
        this.column = column;
        this.type = type;
        this.rotation = rotation;

        //Matrixs of all the type of cards we have:

        int[][] tPathMatrix = {
                {1, 1, 1},
                {0, 0, 0},
                {1, 0, 1}
        };
        int[][] linePathMatrix = {
                {1, 1, 1},
                {0, 0, 0},
                {1, 1, 1}
        };
        int[][] anglePathMatrix = {
                {1, 0, 1},
                {1, 0, 0},
                {1, 1, 1}
        };
        int[][] crossPathMatrix = {
                {1, 0, 1},
                {0, 0, 0},
                {1, 0, 1}
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
