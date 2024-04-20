package ch.uzh.ifi.hase.soprafs24.entity;

public class Bid {
    Suit suit;
    Long amount;

    public Bid(Suit suit, Long amount){
        this.suit = suit;
        this.amount = amount;
    }

    public Suit getSuit() {
        return suit;
    }

    public Long getAmount() {
        return amount;
    }
}
