package Controller;

import Model.*;

public class CardsController {

    private static final int NUM_OF_TYPES = 4;
    private static final int NUM_OF_ROT = 4;

    private Card[][] cards;
    private int value;

    // Constructor, Getters and Setters:

    public CardsController(int value) {
        this.value = value;
        this.cards = cardsCreator();
    }

    public Card[][] getCards() {
        return cards;
    }

    // Methods:

    /**
     * This method generate a matrix of cards to initialize the board
     * @return the Matrix of cards.
     */
    public Card[][] cardsCreator() {
        Card[][] cardsMatrix = new Card[value][value];
        for (int i = 0; i<value; i++) {
            for (int j = 0; j<value; j++) {
                Position pos = new Position(i,j);
                if (i == 0 && j == 0) {
                    cardsMatrix[i][j] = new Card(pos, CardType.ANGLEPATH, Rotation.TWO);
                    cardsMatrix[i][j].setAvailable(false);
                } else if (i == 0 && j == value-1) {
                    cardsMatrix[i][j] = new Card(pos, CardType.ANGLEPATH, Rotation.THREE);
                    cardsMatrix[i][j].setAvailable(false);
                } else if (i == value-1 && j == 0) {
                    cardsMatrix[i][j] = new Card(pos, CardType.ANGLEPATH, Rotation.ONE);
                    cardsMatrix[i][j].setAvailable(false);
                } else if (i == value-1 && j == value-1) {
                    cardsMatrix[i][j] = new Card(pos, CardType.ANGLEPATH, Rotation.FOUR);
                    cardsMatrix[i][j].setAvailable(false);
                } else if (i == value/2 && j == value/2) {
                    cardsMatrix[i][j] = new Card(pos, CardType.CROSSPATH, Rotation.ONE);
                } else {
                    int type = new Randomizer().randomize(NUM_OF_TYPES);
                    int rot = new Randomizer().randomize(NUM_OF_ROT);
                    cardsMatrix[i][j] = new Card(pos, cardTypeCreator(type), rotationCreator(rot));
                }
                int[][] rotatedMatrix = initialRotator(cardsMatrix[i][j].getCardMatrix(), cardsMatrix[i][j].getRotation());
                cardsMatrix[i][j].setCardMatrix(rotatedMatrix);
            }
        }

    return cardsMatrix;
    }

    /**
     * This method associate a random number to a type of card
     * @param type a random number in a given interval
     * @return a CardType associated to that number
     */
    public CardType cardTypeCreator(int type) {

        CardType cardType = CardType.ANGLEPATH;
        switch (type) {
            case 0:
                cardType = CardType.ANGLEPATH;
                break;
            case 1:
                cardType = CardType.CROSSPATH;
                break;
            case 2:
                cardType = CardType.LINEPATH;
                break;
            case 3:
                cardType = CardType.TPATH;
        }
        return cardType;
    }

    /**
     * This method associate a random number to a rotation of the card
     * @param rot a random number in a given interval
     * @return a Rotation associated to that number
     */
    public Rotation rotationCreator(int rot) {
        Rotation rotation = Rotation.ONE;
        switch (rot) {
            case 0:
                rotation = Rotation.ONE;
                break;
            case 1:
                rotation = Rotation.TWO;
                break;
            case 2:
                rotation = Rotation.THREE;
                break;
            case 3:
                rotation = Rotation.FOUR;
        }
        return rotation;
    }

    /**
     * Spin the cards baby! This method just rotates the cards based on them rotation type
     * @param matrix it's the matrix of the single card
     * @param rotationType it's a rotation type
     * @return the matrix of cards, rotated
     */
    public int[][] initialRotator(int[][] matrix, Rotation rotationType) {
        //TODO Extract from this method a clockWiseRotation method
        int rot = 0;
        switch (rotationType) {
            case ONE:
                rot = 1;
                break;
            case TWO:
                rot = 2;
                break;
            case THREE:
                rot = 3;
                break;
            case FOUR:
                rot = 4;
        }
        int[][] tmp = new int[3][3];
        int[][] tmp2 = matrix;
        int newCol = 0;

        for (int k = 0; k<rot-1; k++) {
            for (int i = 0; i<3; i++) {
                switch(i) {
                    case 0:
                        newCol = 2;
                        break;
                    case 1:
                        newCol = 1;
                        break;
                    case 2:
                        newCol = 0;
                        break;
                }
                for (int j = 0; j<3; j++) {
                    tmp[j][newCol] = tmp2[i][j];
                }
            }
            for (int i = 0; i < tmp.length; i++) {
                for (int j = 0; j < tmp.length; j++) {
                    tmp2[i][j]=tmp[i][j];
                }
            }
        }
        if (rot == 1) {
            return matrix;
        } else
            return tmp;
    }


    //TODO check if this method works
    //Ok I honestly feel so bad about how this method is written that I'll do it again.
    /*
    public Card[] cardsSlider(Card[][] cards, Position prevPos, Position newPos, int value) {
        int newPositionId = 0;
        int oldPositionID = 0;
        for (int i = 0; i<cards.length; i++) {
            for (int j = 0; j<cards.length; j++) {
                if (newPos.getColumn() == cards[i][j].getPosition().getColumn() && newPos.getRow() == cards[i][j].getPosition().getRow()) {
                    newPositionId = cards[i].getIdNumber();
                } else if (prevCol == cards[i].getPosition().getColumn() && prevRow == cards[i].getPosition().getRow()) {
                    oldPositionID = cards[i].getIdNumber();
                }
            }
        }
        Card[] toBeMoved = new Card[value];
        Card [] moved = new Card[value];

        //For vertical sliding:
        if (prevCol == newPos.getColumn() && (prevRow == 0 && newPos.getRow() == value || prevRow == value && newPos.getRow() == 0)) {
            int k = 0;
            for (int i = 0; i<cards.length; i++) {
                if (cards[i].getPosition().getColumn() == newPos.getColumn()) {
                    toBeMoved[k] = cards[i];
                    k++;
                }
            }
            cardsMover(value, newPositionId, oldPositionID, toBeMoved, moved);
        }

        //For horizontal sliding:
        else if (prevRow == newPos.getRow() && (prevCol == 0 && newPos.getColumn() == value || prevCol == value && newPos.getColumn() == 0)) {
            int k = 0;
            for (int i = 0; i<cards.length; i++) {
                if (cards[i].getPosition().getRow() == newPos.getRow()) {
                    toBeMoved[k] = cards[i];
                    k++;
                }
            }
            moved = cardsMover(value, newPositionId, oldPositionID, toBeMoved, moved);
        }
        // Final cycle to move cards
        for (int i = 0; i<value; i++) {
            for (int j = 0; j<cards.length; j++) {
                if (cards[j].getIdNumber() == moved[i].getIdNumber()) {
                    moved[i].setColumn(cards[j].getPosition().getColumn());
                    moved[i].setRow(cards[j].getPosition().getRow());
                    cards[j] = moved[i];
                }
            }
        }

        return cards;
    }

    private Card[] cardsMover(int value, int newPositionId, int oldPositionID, Card[] toBeMoved, Card[] moved) {
        if (newPositionId < oldPositionID) {
            int j = 1;
            for (int i = 0; i<value; i++) {
                if (j == value -1) {
                    j = 0;
                }
                moved[j] = toBeMoved[i];
                j++;
            }
        }
        else {
            int j = 0;
            for (int i = 1; i<value; i++) {
                moved[j] = toBeMoved[i];
                j++;
            }
            moved[0] = toBeMoved[0];
        }
        return moved;
    }*/

}
