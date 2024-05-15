package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;


public class Lobby implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    public LinkedHashMap<Long, Player> players;


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
    }

    public Lobby() {
        this.players = new LinkedHashMap<>();
    }

   // public Leaderboard createLeaderboard() {
  //      Leaderboard leaderboard = new Leaderboard();
 //       Map<Player, Long> userPoints = new HashMap<>();
    
 //       if (players != null) {
  //          for (Player player : players) {
                // Hier setzen wir die Punkte des Benutzers auf 0.
 //               userPoints.put(player, 0L);
 //       }
 //   }
    
 //       leaderboard.setUserPoints(userPoints);
 //       return leaderboard;
 //   }

    public void startGame() {
        game = new Game(this);
        //game.startGame();
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private Player convertUserToPlayer(User user) {
        Player player = new Player(user);
        // Setzen Sie hier andere Attribute des Players, falls erforderlich.
        return player;
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
        leaderboard.addUser(player);
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

}
