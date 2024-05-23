package ch.uzh.ifi.hase.soprafs24.managers;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyManager {

    private HashMap<Long, Lobby> lobbies;

    public LobbyManager() {
        this.lobbies = new HashMap<>();
    }

    public void addLobby(Lobby lobby) {
        lobbies.put(lobby.getId(), lobby);
    }

    public Lobby removeLobby(Long id) {
        return lobbies.remove(id);
    }

    public Lobby getLobby(Long id) {
        Lobby lobby = lobbies.get(id);
        if (lobby == null) {
            throw new IllegalArgumentException("Lobby with id " + id + " does not exist.");
        }
        return lobby;
    }

    public List<Lobby> getLobbies() {
        return new ArrayList<>(lobbies.values());
    }

    public Long generateLobbyId() {
        Long maxId = 0L;
        for (Long id : lobbies.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    public void clearAllLobbies() {
        lobbies.clear();
    }
}
