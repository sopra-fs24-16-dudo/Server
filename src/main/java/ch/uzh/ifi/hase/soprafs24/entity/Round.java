package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.RoundState.LibreState;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.RoundState;

import java.util.List;
import java.util.Map;

public class Round {

    private RoundState state;
    private List<Player> players;
    private Player currentPlayer;
    private Player lastPlayer;
    private Player loser;
    private Map<Suit, Long> SuitCounter;
    private Bid currentBid;

    public Round(List<Player> players){
        this.players = players;
        this.state = new LibreState();
        this.currentPlayer = players.get(0);
        this.lastPlayer = players.get(players.size()-1);
        this.loser = null;
        this.SuitCounter = state.getSuitCounter(players);
        this.currentBid = new Bid(Suit.ACE, 1L);
    }

    public void placeBid(Bid bid){
        List<Bid> validBids = state.getValidBids(currentBid, currentPlayer);
        currentBid = state.placeBid(bid, validBids);
    }
    public void dudo(){
        loser = state.dudo(currentBid, SuitCounter, players, currentPlayer, lastPlayer);
    }

    public void setState(RoundState state){
        this.state = state;
    }

}
