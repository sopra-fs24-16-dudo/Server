package ch.uzh.ifi.hase.soprafs24.entity;

public enum Suit {
    NINE(1), TEN(2), JACK(3), QUEEN(4), KING(5), ACE(6);
    private final int value;

    Suit(int value) {
        this.value = value;
    }

    public static Suit getSuit(String suit) {
        if (suit.equals("NINE")) {
            return NINE;
        } else if (suit.equals("TEN")) {
            return TEN;
        } else if (suit.equals("JACK")) {
            return JACK;
        } else if (suit.equals("QUEEN")) {
            return QUEEN;
        } else if (suit.equals("KING")) {
            return KING;
        } else if (suit.equals("ACE")) {
            return ACE;
        } else {
            throw new IllegalArgumentException("Invalid suit");
        }
    }
}
