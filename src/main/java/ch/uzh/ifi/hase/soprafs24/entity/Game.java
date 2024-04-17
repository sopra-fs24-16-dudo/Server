package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;  // Using the lobby ID as the game ID

    @OneToMany
    @JoinColumn(name = "game_id") // Adjust column name as needed
    private List<User> users;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat = new Chat();

    @OneToOne
    @JoinColumn(name = "lobby_id", referencedColumnName = "id")
    private Lobby lobby;

    private boolean started;
    private boolean ended;

    // Add more fields as needed for your game

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    // Add getters and setters for other fields
}