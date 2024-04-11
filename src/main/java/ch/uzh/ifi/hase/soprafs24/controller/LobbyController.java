package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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
        // convert API lobby to internal representation
        Lobby createdLobby = lobbyService.createLobby(userToAdd);
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

    @PostMapping("/lobby/exit/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> exitLobby(@PathVariable Long lobbyId, @RequestBody Long exitUser) {
        User userToRemove = userService.getUserById(exitUser);
        Lobby updatedLobby = lobbyService.removeUser(lobbyId, userToRemove);

        if (updatedLobby == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/lobby/user/{lobbyId}/ready")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUserReadyStatus(@PathVariable Long lobbyId, @RequestBody Long userId) {
        User userToUpdate = userService.getUserById(userId);
        lobbyService.updateUserReadyStatus(lobbyId, userToUpdate);
    }
    @GetMapping("/lobby/user/{lobbyId}/ready")
    public ResponseEntity<Boolean> areAllUsersReady(@PathVariable Long lobbyId) {
        // Check if all users in the lobby are ready
        boolean allUsersReady = lobbyService.areAllUsersReady(lobbyId);

        // Return true if all users are ready, otherwise false
        return ResponseEntity.ok(allUsersReady);
    }
    @PostMapping("/lobby/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable Long lobbyId) {
        // TODO Start the game logic here

        // Reset the readiness status of all users in the lobby to false
        lobbyService.resetAllUsersReadyStatus(lobbyId);
    }

    @PostMapping("/lobby/{lobbyId}/chat")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void postMessage(@PathVariable Long lobbyId, @RequestBody User user, String message) {
        message = user.getUsername() + ": " + message;
        lobbyService.postMessage(lobbyId, message);
    }

    @GetMapping("/lobby/{lobbyId}/chat")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getChatMessages(@PathVariable Long lobbyId) {
        return lobbyService.getMessages(lobbyId);
    }


}
