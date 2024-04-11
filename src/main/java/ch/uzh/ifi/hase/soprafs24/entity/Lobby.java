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


    @OneToOne
    private Chat chat;


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

}
