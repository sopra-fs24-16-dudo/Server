package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "VOICECHANNEL")
public class VoiceChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String channelId;

    private Lobby lobby;

    @ManyToMany(mappedBy = "voiceChannels")
    private Set<User> users = new HashSet<>();

    public VoiceChannel() {
    }

    public VoiceChannel(String channelId) {
        this.channelId = channelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.users.add(user);
        //user.setVoiceChannel(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        //user.setVoiceChannel(null);
    }

    public void clearUsers() {
        this.users.clear();
    }
}
