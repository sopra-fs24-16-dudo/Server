package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;


public class VoiceChannelGetDTO {

    private Long id;
    private Long lobbyId;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }
}