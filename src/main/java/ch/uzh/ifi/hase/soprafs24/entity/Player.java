package ch.uzh.ifi.hase.soprafs24.entity;

public class Player {

    private Hand hand;
    private int chips;
    private boolean disqualified = false;
    private long id;
    private String username;

    public Player(long id, String username) {
        this.id = id;
        this.username = username;
        this.hand = new Hand();
        chips = 3;
    }

    public void roll() {
        hand.roll();
    }

    public void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public Hand getHand() {
        return hand;
    }

    public void subtractChip(){
        chips--;
    }

    public int getChips() {
        return chips;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
