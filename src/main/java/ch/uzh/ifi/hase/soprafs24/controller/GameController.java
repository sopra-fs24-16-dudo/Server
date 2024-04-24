package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.Hand;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class GameController {

    private final LobbyService lobbyService;
    private final UserService userService;

    GameController(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @GetMapping("/games/players/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayers(@PathVariable Long lobbyId) {
        List<Player> players = lobbyService.getPlayersInLobby(lobbyId);
        if (players != null){
            return players;
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No players in game");
        }
    }
    @PostMapping("/games/hand/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Hand rollHand(@PathVariable Long lobbyId, @RequestBody Long userId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player player = lobby.getPlayerById(userId);
        if (player.hasRolled()){
            return player.getHand();
        }
        player.roll();
        return player.getHand();
    }

}

