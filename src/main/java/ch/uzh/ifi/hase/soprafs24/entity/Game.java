package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;


public class Game implements Serializable {

    private LinkedHashMap<Long, Player> players;
    private Player winner;
    private Player startingPlayer;

    private Long lobbyId;

    private boolean isOpen;

    private Round round;

    public Game(Lobby lobby) {
        lobbyId = lobby.getId();
        players = lobby.getPlayers();
        isOpen = true;
        winner = null;
    }

    public void playRound (){
        if (startingPlayer == null){
            startingPlayer = getPlayers().get(0);
        }
        startingPlayer = calculateStartingPlayer();
        round = new Round(getPlayers(), startingPlayer);
    }


    public boolean checkWinner(){
        int notDisqualifiedCount = 0;
        for (Player player : players.values()) {
            if (!player.isDisqualified()) {
                notDisqualifiedCount++;
            }
        }
        return notDisqualifiedCount == 1;
    }

    private void endGame(){
        //set winner
       // for (Player player : players) {
       //     if (!player.isDisqualified()) {
        //        winner = player;
        //    }
       // }
    }

  //  public void startGame(){
        //set starting player
   //     setStartingPlayer(players.get(0));
        //while (!checkWinner()) {
         //   Player loser = playRound(players.values(), startingPlayer);
         //   subtractChips(loser);
         //   setStartingPlayer(loser);
       // }
   // }

    private Player calculateStartingPlayer(){
        if (startingPlayer.getChips() > 0) {
            return startingPlayer;
        } else {
            // Get the list of players
            List<Player> playerList = getPlayers();
            // Find the index of the current starting player
            int currentIndex = playerList.indexOf(startingPlayer);
            // Check the next players
            for (int i = 1; i < playerList.size(); i++) {
                Player nextPlayer = playerList.get((currentIndex + i) % playerList.size());
                if (nextPlayer.getChips() > 1) {
                    return nextPlayer;
                }
            }
            for (int i = 1; i < playerList.size(); i++) {
                Player nextPlayer = playerList.get((currentIndex + i) % playerList.size());
                if (nextPlayer.getChips() == 1) {
                    return nextPlayer;
                }
            }
        }
        // If no player with chips > 0 is found, return null or handle appropriately
        return null;
    }

   // private void subtractChips(Player loser){
     //   loser.subtractChip();
  //  }

    private void setStartingPlayer(Player player){
        //TODO if a player is disqualified, the next player should start
        startingPlayer = player;
    }
    public void setStartPlayer(Player player){
        //TODO if a player is disqualified, the next player should start
        startingPlayer = player;
    }

    public Player getStartingPlayer(){
        return startingPlayer;
    }

    public Player getWinner(){
        return winner;
    }

    public Player getLoser(){
        Player player = null;
        for (Player p : players.values()) {
            if (p.getId() == (round.getLoserId())) {
                player = p;
            }
        }
        return player;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void close(){
        isOpen = false;
    }

    public void open(){
        isOpen = true;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(players.values());
    }

    public Round getRound(){
        return round;
    }

    public Bid getCurrentBid() {
        return round.getCurrentBid();
    }

    public Bid getNextBid() {
        return round.getNextBid();
    }

    public List<Bid> getValidBids() {
        return round.getValidBids();
    }

    public void placeBid(Bid bid){
        round.placeBid(bid);
    }

    public List<Hand> getHands(){
        return round.getHands();
    }

    public void dudo(){
        round.dudo();
        startingPlayer = players.get(round.getLoserId());
        players.get(round.getLoserId()).subtractChip();
    }

    public void setCurrentBid(Bid expectedBid) {
        this.round.getCurrentBid();
    }
}
