package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column
    @OneToMany
    private List<User> users;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voice_channel_id", referencedColumnName = "id")
    private VoiceChannel voiceChannel;

    @OneToOne(cascade = CascadeType.ALL)//previously received Caused by: org.hibernate.AnnotationException: @Column(s) not allowed on a @OneToOne property: ch.uzh.ifi.hase.soprafs24.entity.Lobby.chat
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat = new Chat();

    @Transient
    private Leaderboard leaderboard;

    @Transient
    public List<Player> players;

    @Transient
    private Game game;

    public Game getGame() {
        return game;
    }

    //TODO Implement the delete Player functionality!

    public Lobby() {
        this.leaderboard = createLeaderboard();
    }

    public Leaderboard createLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        Map<User, Long> userPoints = new HashMap<>();
    
        if (users != null) {
            for (User user : users) {
                // Hier setzen wir die Punkte des Benutzers auf 0.
                userPoints.put(user, 0L);
        }    
    }
    
        leaderboard.setUserPoints(userPoints);
        return leaderboard;
    }

    public void startGame() {
        game = new Game(players);
        //game.startGame();
    }

    public List<Player> getPlayers() {
        players = convertUsersToPlayers(users);
        return players;
    }

    private List<Player> convertUsersToPlayers(List<User> users) {
        List<Player> players = new ArrayList<>();
        for (User user : users) {
            Player player = new Player();
            player.setUsername(user.getUsername());
            player.setId(user.getId());
            // Setzen Sie hier andere Attribute des Players, falls erforderlich.
            players.add(player);
        }
        return players;
    }

    public void addUser(User user) {
        users.add(user);
        leaderboard.addUser(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    public VoiceChannel getVoiceChannel() { return voiceChannel; }
    public void setVoiceChannel(VoiceChannel voiceChannel) { this.voiceChannel = voiceChannel; }
}
