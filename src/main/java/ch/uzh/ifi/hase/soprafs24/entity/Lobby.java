package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.RoundState.FijoState;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.LibreState;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.RoundState;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.swing.plaf.nimbus.State;


public class Lobby implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    public LinkedHashMap<Long, Player> players;

    private boolean isOpen;
    //private VoiceChannel voiceChannel;

    private long adminId;
    private Chat chat = new Chat();

    private Leaderboard leaderboard;

    private Game game;

    public Lobby(Long id) {
        this.players = new LinkedHashMap<>();
        this.id = id;
        this.adminId = adminId;
        this.leaderboard = new Leaderboard();
        this.game = new Game(this);
        isOpen = true;
    }

    public Lobby() {
        this.players = new LinkedHashMap<>();
    }


    public void startGame() {
        isOpen = false;
        game = new Game(this);
        //game.startGame();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void addPlayer(Player player) {
        if (isOpen){
        players.put(player.getId(), player);
        leaderboard.addUser(player);
        }
    }

    public void deletePlayer(Long playerId) {
        players.remove(playerId);
    }


    public Chat getChat() {
        return chat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdminId() {
        return adminId;
    }
    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public LinkedHashMap<Long, Player> getPlayers() {
        return players;
    }

    public List<Player> getPlayersList() {
        return new ArrayList<>(players.values());
    }

    public Player getPlayerById(Long playerId) {
        return players.get(playerId);
    }

    public List<Player> getPlayersInGame() {
        return game.getPlayers();
    }

    public void startRound(){
        setWinner(null);
        game.playRound();
    }

    public Round getRound(){
        return game.getRound();
    }

    public Bid getCurrentBid(){
        if (game.getRound() != null)
            return game.getRound().getCurrentBid();
        return game.getCurrentBid();
    }

    public Player getCurrentPlayer(){
        return game.getCurrentPlayer();
    }


    public Bid getNextBid(){
        return game.getNextBid();
    }

    public List<Bid>getValidBids(){
        return game.getValidBids();
    }

    public void placeBid(Bid bid){
        game.placeBid(bid);
    }

    public void dudo(){
        game.dudo();
    }

    public Player getWinner(){
        Player winner = game.getWinner();
        return winner;
    }

    public boolean checkWinner(){
        return game.checkWinner();
    }

    public Player getLoser(){
        return game.getLoser();
    }

    public List<Hand> getHands(){
        return game.getHands();
    }

    public void setWinner(Player winner) {
        game.setWinner(winner);
    }

    public void updatePoints(Player winner) {
        isOpen = true;
        if (winner != null){
            if (winner.getChips() == 3) {
                leaderboard.addPoints(winner, 2L);
            }
            else {
                leaderboard.addPoints(winner, 1L);
            }
        }
    }
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public boolean isFijo() {
        return game.getRound().getState() instanceof FijoState;
    }

    // New methods for testing
    public void postMessage(String message) {
        chat.addMessage(message);
    }

    public List<String> getChatMessages() {
        return chat.getMessages();
    }

    public void setPlayers(LinkedHashMap<Long, Player> players) {
        this.players = players;
    }

}
