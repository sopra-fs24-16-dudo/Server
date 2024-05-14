package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Player;

import java.util.LinkedHashMap;

public class LobbyGetDTO {

    private Long id;
    private Long adminId;
    private LinkedHashMap<Long, Player> players;
    private Bid currentBid;
    private Bid nextBid;
    private Player winner;
    private Player currentPlayer;

    //setter and getter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() { return adminId; }

    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public LinkedHashMap<Long, Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedHashMap<Long, Player> players){
        this.players = players;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Bid currentBid) {
        this.currentBid = currentBid;
    }

    public Bid getNextBid(){
        return nextBid;
    }

    public void setNextBid(Bid nextBid){
        this.nextBid = nextBid;
    }

    public Player getWinner(){
        return winner;
    }

    public void setWinner(Player winner){
        this.winner = winner;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

}
