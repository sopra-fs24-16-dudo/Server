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


@RestController
public class GameController {

    private final LobbyService lobbyService;
    private final UserService userService;
    private final GameService gameService;

    GameController(GameService gameService, LobbyService lobbyService, UserService userService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @GetMapping("/games/players/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayers(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        List<Player> players = gameService.getPlayers(lobby);
        if (players != null){
            return players;
        }
        else{
            throw new IllegalArgumentException("No players in game");
        }
    }
    @PostMapping("/game/hand/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Hand rollHand(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Hand hand = new Hand();
        hand.roll();

        return hand;
    }

}

