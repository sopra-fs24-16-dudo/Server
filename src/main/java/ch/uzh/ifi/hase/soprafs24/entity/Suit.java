package ch.uzh.ifi.hase.soprafs24.entity;

public enum Suit {
    NINE(1), TEN(2), JACK(3), QUEEN(4), KING(5), ACE(6);

    private final int value;

    Suit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
