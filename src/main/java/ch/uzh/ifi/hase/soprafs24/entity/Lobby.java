package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


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
