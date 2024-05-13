package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.RoundState.FijoState;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.LibreState;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.RoundState;
import java.util.Collection;

import java.util.List;
import java.util.Map;

public class Round {

    private RoundState state = null;
    private List<Player> players;
    private Player currentPlayer;
    private Player lastPlayer;
    private Player loser;
    private Map<Suit, Long> SuitCounter;
    private Bid currentBid;

    public Round(List<Player> players, Player startingPlayer){
        this.players = players;
        this.currentPlayer = startingPlayer;
        setState(startingPlayer);
        this.lastPlayer = null;
        this.loser = null;
        this.currentBid = new Bid();
        for (Player player : players) {
            player.roll();
        }
        this.SuitCounter = state.getSuitCounter(players);
    }

    public Round(){
        this.players = null;
        this.currentPlayer = null;
        this.lastPlayer = null;
        this.loser = null;
        this.currentBid = null;
        this.SuitCounter = null;
    }

    public void placeBid(Bid bid){
        List<Bid> validBids = state.getValidBids(currentBid, currentPlayer, (long) players.size());
        currentBid = state.placeBid(bid, validBids);
        setLastPlayer(currentPlayer);
        setNextPlayer();
    }
    public void dudo(){
        Long loserId = state.dudo(currentBid, SuitCounter, currentPlayer, lastPlayer);
        for (Player player : players) {
            if (player.getId() == loserId){
                loser = player;
            }
        }
    }

    public void setState(Player startingPlayer){
        if (startingPlayer.getChips() > 1) {
            this.state = new LibreState();
        }
        else {
            this.state = new FijoState();
        }
    }

    public Long getLoserId(){
        return loser.getId();
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public Bid getNextBid(){
        if (this.players == null)
            return new Bid(Suit.NINE, 1L);
        return state.getNextBid(currentBid, currentPlayer, (long) players.size());
    }

    public List<Bid> getValidBids(){
        return state.getValidBids(currentBid, currentPlayer, (long) players.size());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getLastPlayer() {
        return lastPlayer;
    }

    public void setNextPlayer(){
        int index = players.indexOf(currentPlayer);
        for (int i = 1; i < players.size(); i++) {
            currentPlayer = players.get((index + i) % players.size());
            if (currentPlayer.getChips() > 0){
                return;
            }
        }
    }

    public void setLastPlayer(Player player) {
        //find the player in the list of players that matches the ID of player
        for (Player p : players) {
            if (p.getId() == (player.getId())) {
                lastPlayer = p;
            }
        }
    }

    public Map<Suit, Long> getSuitCounter() {
        return SuitCounter;
    }

    public List<Hand> getHands(){
        List<Hand> hands = null;
        for (Player player : players) {
            hands.add(player.getHand());
        }
        return hands;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setCurrentBid(Bid bid) {this.currentBid = bid;}

    @Override
    public String toString() {
        return "Round{" +
                "state=" + state +
                ", players=" + players +
                ", currentPlayer=" + currentPlayer +
                ", SuitCounter=" + SuitCounter +
                ", currentBid=" + currentBid +
                '}';
    }



}
