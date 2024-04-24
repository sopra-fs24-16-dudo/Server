package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;


public class Lobby implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    public LinkedHashMap<Long, Player> players;


    //private VoiceChannel voiceChannel;


    private Chat chat = new Chat();


    private Leaderboard leaderboard;

    private Game game;

    public Lobby(Long id) {
        this.players = new LinkedHashMap<>();
        this.id = id;
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


    private Player convertUserToPlayer(User user) {
        Player player = new Player(user);
        // Setzen Sie hier andere Attribute des Players, falls erforderlich.
        return player;
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
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

    //public VoiceChannel getVoiceChannel() { return voiceChannel; }
    //public void setVoiceChannel(VoiceChannel voiceChannel) { this.voiceChannel = voiceChannel; }

    public LinkedHashMap<Long, Player> getPlayers() {
        return players;
    }

    public List<Player> getPlayersList() {
        return new ArrayList<>(players.values());
    }

    public Player getPlayerById(Long playerId) {
        return players.get(playerId);
    }


}
