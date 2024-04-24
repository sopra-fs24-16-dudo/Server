package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserService userService;


    LobbyController(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;

    }
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        // fetch all users in the internal representation
        List<Lobby> lobbies = lobbyService.getAllLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Lobby lobby : lobbies) {
            lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOs;
    }
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody Long newUser) {
        User userToAdd = userService.getUserById(newUser);
        Player player = lobbyService.createPlayer(userToAdd);
        // convert API lobby to internal representation
        Lobby createdLobby = lobbyService.createLobby(player);
        // create lobby
        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobby/players/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResponseEntity<?> addPlayerToLobby(@PathVariable Long lobbyId, @RequestBody Long newUser) {
        User userToAdd = userService.getUserById(newUser);
        Player player = lobbyService.createPlayer(userToAdd);
        if (lobbyService.playerInLobby(lobbyId, player)) {
            throw new IllegalArgumentException("Player already in lobby");
        }
        Lobby updatedLobby = lobbyService.addPlayer(lobbyId, player);
        if (updatedLobby == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/lobby/players/{lobbyId}") // Corrected the path variable syntax
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayersFromLobby(@PathVariable Long lobbyId) {
        return lobbyService.getPlayersInLobby(lobbyId);
    }

    @PostMapping("/lobby/exit/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void exitLobby(@PathVariable Long lobbyId, @RequestBody Long playerId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.removePlayer(lobby, playerId);
    }
    @PutMapping("/lobby/player/{lobbyId}/ready")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUserReadyStatus(@PathVariable Long lobbyId, @RequestBody Long userId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player player = lobby.getPlayerById(userId);
        lobbyService.updatePlayerReadyStatus(player);
    }
    @GetMapping("/lobby/player/{lobbyId}/ready")
    public ResponseEntity<Boolean> areAllPlayerReady(@PathVariable Long lobbyId) {
        // Check if all users in the lobby are ready
        boolean allUsersReady = lobbyService.allPlayersReady(lobbyId);
        // Return true if all users are ready, otherwise false
        return ResponseEntity.ok(allUsersReady);
    }

    @PostMapping("/lobby/chat/{lobbyId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void postMessage(@PathVariable Long lobbyId,@PathVariable Long userId, @RequestBody String message) {
        User user = userService.getUserById(userId);
        String MyMessage = user.getUsername() + ": " + message;
        lobbyService.postMessage(lobbyId, MyMessage);
    }

    @GetMapping("/lobby/chat/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getChatMessages(@PathVariable Long lobbyId) {
        return lobbyService.getMessages(lobbyId);
    }

    @GetMapping("/rules") // Corrected the path variable syntax
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getRules() {
        return lobbyService.getRules();
    }

    @PostMapping("/lobby/start/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void gameStarter(@PathVariable Long lobbyId) {
        lobbyService.startGame(lobbyId);
    }
}
