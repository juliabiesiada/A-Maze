package Controller;

import Model.Card;
import Model.CardType;
import Model.Rotation;

public class CardsController {

    private Card[] cards;
    private int value;

    public CardsController(int value) {
        this.value = value;
        this.cards = cardsCreator();
    }

    public Card[] cardsCreator() {

        int numOfCards = value * value;
        int k = 0;
        Card[] cardsArray = new Card[numOfCards];
        for (int i = 0; i<value; i++) {
            for (int j = 0; j<value; j++) {
                if (i == 0 && j == 0) {
                    cardsArray[k] = new Card(k, i, j, CardType.ANGLEPATH, Rotation.TWO);
                } else if (i == 0 && j == value-1) {
                    cardsArray[k] = new Card(k, i, j, CardType.ANGLEPATH, Rotation.THREE);
                } else if (i == value-1 && j == 0) {
                    cardsArray[k] = new Card(k, i, j, CardType.ANGLEPATH, Rotation.ONE);
                } else if (i == value-1 && j == value-1) {
                    cardsArray[k] = new Card(k, i, j, CardType.ANGLEPATH, Rotation.FOUR);
                } else if (i == value/2 && j == value/2) {
                    cardsArray[k] = new Card(k, i, j, CardType.CROSSPATH, Rotation.ONE);
                } else {
                    int type = randomizer();
                    int rot = randomizer();
                    cardsArray[k] = new Card(k, i, j, cardTypeCreator(type), rotationCreator(rot));
                }
                k++;
            }
        }

        for (int i = 0; i<cardsArray.length; i++) {
        int[][] rotatedMatrix = initialRotator(cardsArray[i].getCardMatrix(), cardsArray[i].getRotation());
        cardsArray[i].setCardMatrix(rotatedMatrix);
        }
    return cardsArray;
    }

    public int randomizer() {
        int n = (int) (Math.random() * 4);
        return n;
    }

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

    public int[][] initialRotator(int[][] matrix, Rotation rotationType) {
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

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
