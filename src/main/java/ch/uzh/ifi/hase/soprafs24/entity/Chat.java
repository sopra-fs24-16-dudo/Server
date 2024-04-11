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
    private List<String> messages = new ArrayList<>();;//ensure not giving a null further down the line and change from ArrayList as it is not supported by hibernate


    public List<String> getMessages() {
        return messages;
    }// change from ArrayList as it is not supported by hibernate

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
