package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


public class Game implements Serializable {
    private static final long serialVersionUID = 1L;


    private List<Player> players;
    private Chat chat = new Chat();
    private Player winner;
    private Player startingPlayer;

    private Player playRound (List<Player> players, Player startingPlayer){
        //returns the loser of the round
        return null;
    }

    private boolean checkWinner(){
        int notDisqualifiedCount = 0;
        for (Player player : players) {
            if (!player.isDisqualified()) {
                notDisqualifiedCount++;
            }
        }
        return notDisqualifiedCount == 1;
    }

    private void endGame(){
        //set winner
        for (Player player : players) {
            if (!player.isDisqualified()) {
                winner = player;
            }
        }
    }

    private void startGame(){
        //set starting player
        setStartingPlayer(players.get(0));
        while (!checkWinner()) {
            Player loser = playRound(players, startingPlayer);
            subtractChips(loser);
            setStartingPlayer(loser);
        }
    }

    private void subtractChips(Player loser){
        loser.subtractChip();
    }

    private void setStartingPlayer(Player player){
        //TODO if a player is disqualified, the next player should start
        startingPlayer = player;
    }

    public Player getStartingPlayer(){
        return startingPlayer;
    }

    public List<Player> getUsers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

}
