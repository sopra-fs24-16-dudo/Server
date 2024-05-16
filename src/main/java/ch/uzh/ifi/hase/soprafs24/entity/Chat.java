package ch.uzh.ifi.hase.soprafs24.entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CHAT")
public class Chat {

    @GeneratedValue
    @Id
    private Long id;

    @ElementCollection
    private List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
