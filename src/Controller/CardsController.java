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
                    cardsArray[k] = new Card(k, i, j, CardType.LINEPATH, Rotation.ONE);
                    //this has to be randomized
                }
                k++;
            }

        }
    return cardsArray;
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
