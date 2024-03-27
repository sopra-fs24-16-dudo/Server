package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserService userService;

    LobbyController(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby() {
        // convert API lobby to internal representation
        Lobby createdLobby = lobbyService.createLobby();
        // create lobby
        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PutMapping("/lobby/user/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResponseEntity<?> addUsertoLobby(@PathVariable Long lobbyId, @RequestBody Long newUser) {
        User userToAdd = userService.getUserById(newUser);
        Lobby updatedLobby = lobbyService.addUser(lobbyId, userToAdd);

        if (updatedLobby == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/lobby/user/{lobbyId}") // Corrected the path variable syntax
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<User> getUsersInLobby(@PathVariable Long lobbyId) {
        return lobbyService.getUsersInLobby(lobbyId);
    }
}
