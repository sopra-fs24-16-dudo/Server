package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    LobbyController(LobbyService lobbyService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        List<Lobby> lobbies = lobbyService.getAllLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();
        for (Lobby lobby : lobbies) {
            LinkedHashMap<Long, Player> players = lobby.getPlayers();
            LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
            lobbyGetDTO.setPlayers(players);

        }
        return lobbyGetDTOs;
    }
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody Long newUser) {
        User userToAdd = userService.getUserById(newUser);
        Player player = lobbyService.createPlayer(userToAdd);
        Lobby createdLobby = lobbyService.createLobby(player);
        messagingTemplate.convertAndSend("/topic/lobby" + createdLobby.getId(), DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby));

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobbies/players/{lobbyId}")
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
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby));

        return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/lobbies/players/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayersFromLobby(@PathVariable Long lobbyId) {
        return lobbyService.getPlayersInLobby(lobbyId);
    }

    @PostMapping("/lobbies/exit/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void exitLobby(@PathVariable Long lobbyId, @RequestBody Long playerId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.removePlayer(lobby, playerId);
        if (lobby.getPlayersList().size() == 1) {
            lobby.open();
        }
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }
    @PostMapping("/lobbies/kick/{lobbyId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void kickPlayerFromLobby(@PathVariable Long lobbyId, @RequestBody Long playerId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        messagingTemplate.convertAndSend("/topic/kick/" + playerId,"You have been kicked from the lobby");
        lobbyService.removePlayer(lobby, playerId);
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }
    @PutMapping("/lobbies/player/{lobbyId}/ready")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUserReadyStatus(@PathVariable Long lobbyId, @RequestBody Long userId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player player = lobby.getPlayerById(userId);
        lobbyService.updatePlayerReadyStatus(player);
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }
    @GetMapping("/lobbies/player/{lobbyId}/ready")
    public ResponseEntity<Boolean> areAllPlayerReady(@PathVariable Long lobbyId) {
        boolean allUsersReady = lobbyService.allPlayersReady(lobbyId);
        return ResponseEntity.ok(allUsersReady && lobbyService.getPlayersInLobby(lobbyId).size() > 1);
    }


    @GetMapping("/rules")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getRules() {
        return lobbyService.getRules();
    }

    @PostMapping("/lobbies/start/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void gameStarter(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobbyService.startGame(lobbyId);
        for (Player player : lobby.getPlayersList()) {
            User user = userService.getUserById(player.getId());
            user.incrementGamesPlayed();
        }
        lobbyService.startRound(lobbyId);
        messagingTemplate.convertAndSend("/topic/start/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));

    }

    @GetMapping("/lobbies/{lobbyId}/round")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public String getRound(@PathVariable Long lobbyId) {
        return lobbyService.getRound(lobbyId).toString();
    }

    @GetMapping("/leaderboard/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getLeaderboard(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobbyService.getLeaderboard(lobby).toString();
    }

    @GetMapping("/lobbies/availability/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean isLobbyOpen(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobby.isOpen();
    }

    @GetMapping("/lobbies/admin/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public long getAdmin(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobby.getAdminId();
    }

    @GetMapping("/users/{userId}/lobby")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Long> getUserLobby(@PathVariable Long userId) {
        Long lobbyId = lobbyService.findLobbyByUserId(userId);
        if (lobbyId != null) {
            return ResponseEntity.ok(lobbyId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
