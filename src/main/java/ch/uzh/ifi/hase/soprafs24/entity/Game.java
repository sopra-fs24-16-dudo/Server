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

    public Game(Lobby lobby) {
        lobbyId = lobby.getId();
        players = lobby.getPlayers();
        isOpen = true;
    }

    private Player playRound (List<Player> players, Player startingPlayer){
        //returns the loser of the round
        return null;
    }

    /**
    private boolean checkWinner(){
        int notDisqualifiedCount = 0;
        for (Player player : players) {
            if (!player.isDisqualified()) {
                notDisqualifiedCount++;
            }
        }
        return notDisqualifiedCount == 1;
    }
     */

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

   // private void subtractChips(Player loser){
     //   loser.subtractChip();
  //  }

    private void setStartingPlayer(Player player){
        //TODO if a player is disqualified, the next player should start
        startingPlayer = player;
    }

    public Player getStartingPlayer(){
        return startingPlayer;
    }

    public Player getWinner(){
        return winner;
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


}
