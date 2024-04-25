package ch.uzh.ifi.hase.soprafs24.entity;

public class Bid {
    Suit suit;
    Long amount;

    public Bid(Suit suit, Long amount){
        this.suit = suit;
        this.amount = amount;
    }

    public Bid(){
        this.suit = null;
        this.amount = null;
    }

    public Bid(String bid){
        String[] parts = bid.split(" ");
        this.amount = Long.parseLong(parts[0]);
        this.suit = Suit.getSuit(parts[1]);
    }

    public Suit getSuit() {
        return suit;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return amount + " " + suit;
    }
}