package ch.uzh.ifi.hase.soprafs24.entity;

public class Player {

    private Hand hand;
    private int chips;
    private boolean disqualified;
    private long id;
    private String username;

    private boolean ready;
    private boolean rolled;

    public Player(User user) {
        this.hand = new Hand();
        rolled = false;
        this.chips = 3;
        this.id = user.getId();
        this.username = user.getUsername();
        disqualified = false;
        ready = false;
    }


    public void roll() {
        hand.roll();
        rolled = true;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long countSuit (Suit suit){
        return hand.countSuit(suit);
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean hasRolled() {
        return rolled;
    }


}
