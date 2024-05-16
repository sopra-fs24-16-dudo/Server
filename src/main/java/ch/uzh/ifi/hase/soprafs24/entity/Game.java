package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.RoundState.RoundState;

import javax.swing.plaf.nimbus.State;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


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
        for (Player player : players.values()) {
            player.resetChips();
            player.resetDisqualified();
            player.setReady(false);
        }
        isOpen = true;
        winner = null;
        round = new Round();
    }

    public void playRound() {
        if (startingPlayer == null) {
            startingPlayer = getPlayers().get(0);
        }
        startingPlayer = calculateStartingPlayer();
        round = new Round(getPlayers(), startingPlayer);
    }


    public boolean checkWinner() {
        int notDisqualifiedCount = 0;
        for (Player player : players.values()) {
            if (!player.isDisqualified()) {
                notDisqualifiedCount++;
            }
        }
        if (notDisqualifiedCount == 1) {
            for (Player player : players.values()) {
                if (!player.isDisqualified()) {
                    setWinner(player);
                    return true;
                }
            }
        }
        return false;
    }

    public Player calculateStartingPlayer() {
        if (startingPlayer == null) {
            startingPlayer = players.values().iterator().next();
            return startingPlayer;
        }
        if (startingPlayer.getChips() > 0) {
            return startingPlayer;
        }
        else {
            List<Player> playerList = getPlayers();
            int currentIndex = playerList.indexOf(startingPlayer);
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
        return null;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        winner = player;
    }

    public Player getLoser() {
        if (round.getLoserId() == null) {
            return null;
        }
        Player player = null;
        for (Player p : players.values()) {
            if (p.getId() == (round.getLoserId())) {
                player = p;
            }
        }
        return player;
    }


    public List<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public Round getRound() {
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

    public void placeBid(Bid bid) {
        round.placeBid(bid);
    }

    public List<Hand> getHands() {
        return round.getHands();
    }

    public void dudo() {
        round.dudo();
        startingPlayer = players.get(round.getLoserId());
        players.get(round.getLoserId()).subtractChip();
        if (players.get(round.getLoserId()).getChips() == 0) {
            players.get(round.getLoserId()).disqualify();
        }
    }

    public Player getCurrentPlayer(){
        return round.getCurrentPlayer();
    }

    public void setPlayers(LinkedHashMap<Long, Player> players) {
        this.players = players;
    }

}
