package ch.uzh.ifi.hase.soprafs24.entity;

public class Dice {
    private Suit suit;

    public Dice() {

    }

    public void roll() {
        int random = (int) (Math.random() * 6 + 1);
        switch (random) {
            case 1:
                suit = Suit.NINE;
                break;
            case 2:
                suit = Suit.TEN;
                break;
            case 3:
                suit = Suit.JACK;
                break;
            case 4:
                suit = Suit.QUEEN;
                break;
            case 5:
                suit = Suit.KING;
                break;
            case 6:
                suit = Suit.ACE;
                break;
        }
    }

    public Suit getSuit() {
        return suit;
    }
}
