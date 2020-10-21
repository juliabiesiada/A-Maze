package Controller;

import Model.*;
import javafx.geometry.Pos;

import java.util.concurrent.Callable;

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
     * Spin the cards baby! This method just rotates the cards based on their rotation type
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

        int[][] rotatedMatrix = matrix;
        if (rot == 1) {
          return matrix;
        } else {
            for (int k = 0; k<rot-1; k++) {
                rotatedMatrix = clockWiseRotation(rotatedMatrix);
            }
          return rotatedMatrix;
        }
    }

    /**
     * This method does just a clock wise rotation
     * @param matrix the matrix to be rotated
     * @return the matrix rotated
     */
    public static int[][] clockWiseRotation(int[][] matrix) {
        int[][] tmp = new int[3][3];
        int[][] tmp2 = matrix;
        int newCol = 0;
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
        return tmp;
    }


    /**
     * This precious method, is the most clear I've ever written and I'm so proud of it. I doubt that somebody is
     * reading all the JavaDoc so.. by the way, this method slide an entire column or row
     * (I'm reading, proud of you!)
     * @param cards the cards matrix
     * @param prevPos the position where the drag has started
     * @param newPos the position where the drop landed
     * @param value the grid dimension
     * @return the changed cards matrix
     */
    public static Game cardsSlider(Game game, Card[][] cards, Position prevPos, Position newPos, int value) {
        Card[] slidedLine = new Card[value];
        Position pos = new Position(0,0);
        for (int i = 0; i<value; i++) {
            slidedLine[i] = (new Card(pos, CardType.ANGLEPATH, Rotation.ONE));
        }
        //VERTICAL SLIDE:
        if (prevPos.getColumn() == newPos.getColumn()) {
            if ((prevPos.getRow() == 0 && newPos.getRow() == value-1) || (prevPos.getRow() == value-1 && newPos.getRow() == 0)) {
                //top to bottom
                if (prevPos.getRow() < newPos.getRow()) {
                    slidedLine[value - 1] = cards[prevPos.getRow()][prevPos.getColumn()];
                    slidedLine[value - 1].setPosition(new Position(newPos.getRow(), newPos.getColumn()));
                    if (cards[prevPos.getRow()][prevPos.getColumn()].getOnCard() != OnCard.NOTHING) {
                        game = replaceObject(game, cards[prevPos.getRow()][prevPos.getColumn()].getOnCard(), prevPos, slidedLine[value - 1].getPosition());
                    }
                    for (int i = 0; i < value - 1; i++) {
                        slidedLine[i] = cards[i + 1][prevPos.getColumn()];
                        slidedLine[i].setPosition(new Position(i, prevPos.getColumn()));
                        if (cards[i + 1][prevPos.getColumn()].getOnCard() != OnCard.NOTHING) {
                            game = replaceObject(game, cards[i + 1][prevPos.getColumn()].getOnCard(), new Position(i + 1, prevPos.getColumn()), slidedLine[i].getPosition());
                        }
                    }
                }
                //bottom to top
                else {
                    slidedLine[0] = cards[prevPos.getRow()][prevPos.getColumn()];
                    slidedLine[0].setPosition(new Position(0, newPos.getColumn()));
                    if (cards[prevPos.getRow()][prevPos.getColumn()].getOnCard() != OnCard.NOTHING) {
                        game = replaceObject(game, cards[prevPos.getRow()][prevPos.getColumn()].getOnCard(), prevPos, slidedLine[0].getPosition());
                    }
                    for (int i = 1; i < value; i++) {
                        slidedLine[i] = cards[i - 1][prevPos.getColumn()];
                        slidedLine[i].setPosition(new Position(i, prevPos.getColumn()));
                        if (cards[i - 1][prevPos.getColumn()].getOnCard() != OnCard.NOTHING) {
                            game = replaceObject(game, cards[i - 1][prevPos.getColumn()].getOnCard(), new Position(i - 1, prevPos.getColumn()), slidedLine[i].getPosition());
                        }
                    }
                }
                for (int i = 0; i<value; i++) {
                    cards[i][prevPos.getColumn()] = slidedLine[i];
                }
            }
        }

        //HORIZONTAL SLIDE
        else if (prevPos.getRow() == newPos.getRow()) {
            if ((prevPos.getColumn() == 0 && newPos.getColumn() == value-1) || (prevPos.getColumn() == value-1 && newPos.getColumn() == 0)) {
                //left to right
                if (prevPos.getColumn() < newPos.getColumn()) {
                    slidedLine[value - 1] = cards[prevPos.getRow()][prevPos.getColumn()];
                    slidedLine[value - 1].setPosition(new Position(newPos.getRow(), newPos.getColumn()));
                    if (cards[prevPos.getRow()][prevPos.getColumn()].getOnCard() != OnCard.NOTHING) {
                        game = replaceObject(game, cards[prevPos.getRow()][prevPos.getColumn()].getOnCard(), prevPos, slidedLine[value - 1].getPosition());
                    }
                    for (int i = 0; i < value - 1; i++) {
                        slidedLine[i] = cards[prevPos.getRow()][i+1];
                        slidedLine[i].setPosition(new Position(prevPos.getRow(), i));
                        if (cards[prevPos.getRow()][i + 1].getOnCard() != OnCard.NOTHING) {
                            game = replaceObject(game, cards[prevPos.getRow()][i + 1].getOnCard(), new Position(prevPos.getRow(), i + 1), slidedLine[i].getPosition());
                        }
                    }
                }

                //right to left
                else {
                    slidedLine[0] = cards[prevPos.getRow()][prevPos.getColumn()];
                    slidedLine[0].setPosition(new Position(newPos.getRow(), 0));
                    if (cards[prevPos.getRow()][prevPos.getColumn()].getOnCard() != OnCard.NOTHING) {
                        game = replaceObject(game, cards[prevPos.getRow()][prevPos.getColumn()].getOnCard(), prevPos, slidedLine[0].getPosition());
                    }
                    for (int i = 1; i < value; i++) {
                        slidedLine[i] = cards[prevPos.getRow()][i - 1];
                        slidedLine[i].setPosition(new Position(prevPos.getRow(), i));
                        if (cards[prevPos.getRow()][i - 1].getOnCard() != OnCard.NOTHING) {
                            game = replaceObject(game, cards[prevPos.getRow()][i - 1].getOnCard(), new Position(prevPos.getRow(), i - 1), slidedLine[i].getPosition());
                        }
                    }
                }
                for (int i = 0; i<value; i++) {
                    cards[prevPos.getRow()][i] = slidedLine[i];
                }
            }
        }
        game.setCardsOnBoard(cards);
        return game;
    }

    private static Game replaceObject(Game game, OnCard onCard, Position objPos, Position newObjPos) {
        System.out.println(onCard);
        System.out.println(objPos.getRow() + "" + objPos.getColumn() + " " + newObjPos.getRow() + "" + newObjPos.getColumn());
        switch (onCard) {
            case BUFFER:
                for (int i = 0; i<game.getBuffPositions().size(); i++) {
                    if (game.getBuffPositions().get(i).getRow() == objPos.getRow() &&
                            game.getBuffPositions().get(i).getColumn() == objPos.getColumn()) {
                        game.getBuffPositions().remove(i);
                        game.getBuffPositions().add(newObjPos);
                    }
                }
                break;
            case DEBUFFER:
                for (int i = 0; i<game.getDebuffPositions().size(); i++) {
                    if (game.getDebuffPositions().get(i).getRow() == objPos.getRow() &&
                            game.getDebuffPositions().get(i).getColumn() == objPos.getColumn()) {
                        //System.out.println("sono stronzissimo");
                        game.getDebuffPositions().remove(i);
                        game.getDebuffPositions().add(newObjPos);
                    }
                }
                break;
            case PLAYER:
                for (Player player : game.getPlayers()) {
                    //System.out.println(player.getName() + player.getPosition().getRow() + player.getPosition().getColumn());
                    if (player.getPosition().getRow() == objPos.getRow() && player.getPosition().getColumn() == objPos.getColumn()) {
                        player.setPosition(newObjPos);
                    }
                    //System.out.println(player.getName() + player.getPosition().getRow() + player.getPosition().getColumn());
                }
                break;
            case GEM:
                for (Gem gem : game.getGems()) {
                    if (gem.getPosition().getRow() == objPos.getRow() && gem.getPosition().getColumn() == objPos.getColumn()) {
                        gem.setPosition(newObjPos);
                    }
                }
                break;
            case PLAYER_AND_GEM:
                for (Player player : game.getPlayers()) {
                    if (player.getPosition().getRow() == objPos.getRow() && player.getPosition().getColumn() == objPos.getColumn()) {
                        player.setPosition(newObjPos);
                    }
                }
                for (Gem gem : game.getGems()) {
                    if (gem.getPosition().getRow() == objPos.getRow() && gem.getPosition().getColumn() == objPos.getColumn()) {
                        gem.setPosition(newObjPos);
                    }
                }
                break;
            case STAIRS:
                break;
        }
        return game;
    }


}
