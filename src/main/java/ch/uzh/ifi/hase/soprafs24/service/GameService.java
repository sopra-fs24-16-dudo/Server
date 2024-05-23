package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import java.util.List;

@Service
@Transactional
public class GameService {

    private final LobbyService lobbyService;

    GameService(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    public Player createPlayer (User user){
        return new Player(user);
    }
}