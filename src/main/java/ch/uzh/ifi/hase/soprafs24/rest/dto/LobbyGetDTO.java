package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.entity.Player;

public class LobbyGetDTO {

    private Long id;
    private PlayerGetDTO[] players;

    //setter and getter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerGetDTO[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerGetDTO[] players) {
        this.players = players;
    }
}
