package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class LobbyService {


    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    private UserService userService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserService userService) {
        this.lobbyRepository = lobbyRepository;
        this.userService = userService;
    }

    public Lobby getLobbyById(Long lobbyId) {
        Optional<Lobby> optionalLobby = lobbyRepository.findById(lobbyId);
        Lobby foundLobby = optionalLobby.orElse(null);
        if (foundLobby != null){
            return foundLobby;
        } else {
            return null;
        }
    }

    public List<Lobby> getAllLobbies() {
        return lobbyRepository.findAll();
    }

    public Lobby createLobby(User newUser) {

        Lobby newLobby = new Lobby();
        checkIfLobbyExists(newLobby.getId());

        // Initialize currentUsers as an empty list
        List<User> currentUsers = new ArrayList<>();
        // Add the new user to the list
        currentUsers.add(newUser);
        newLobby.setUsers(currentUsers);

        lobbyRepository.save(newLobby);
        lobbyRepository.flush();
        log.debug("Created Information for Lobby: {}", newLobby.getId());
        return newLobby;
    }

    public Lobby addUser(Long lobbyId, User newUser) {
        Lobby updatedlobby = getLobbyById(lobbyId);

        if (updatedlobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist!");
        }

        if (checkIfUserInLobby(lobbyId, newUser) != null){
            return updatedlobby;
        }

        checkIfLobbyFull(lobbyId);

         // Retrieve the current list of users
        List<User> currentUsers = updatedlobby.getUsers();
        // Add the new user to the list
        currentUsers.add(newUser);
        // Set the updated list of users back to the lobby
        updatedlobby.setUsers(currentUsers);

        // Save the updated lobby
        lobbyRepository.save(updatedlobby);
        lobbyRepository.flush();
        log.debug("Added user to Lobby: {}", updatedlobby.getId());

        return updatedlobby;
    }

    //check if lobby with id is full (max. 6 users)
    private void checkIfLobbyFull(Long lobbyId){
        Optional<Lobby> optionalLobby = lobbyRepository.findById(lobbyId);
        Lobby lobby = optionalLobby.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found"));

        List<User> users = lobby.getUsers();
        int numberOfUsers = users.size();

        if (numberOfUsers >= 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is full");
        }
    }

    //check if lobby with id already exists
    private void checkIfLobbyExists(Long lobbyId) {
        Optional<Lobby> lobby = lobbyRepository.findById(lobbyId);
        if (lobby.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby with id already exists");
        }

    }

    public List<User> getUsersInLobby(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));
        return lobby.getUsers();
    }

    private User checkIfUserInLobby(Long lobbyId, User user) {

        Lobby lobby = getLobbyById(lobbyId);
        List<User> currentUsers = lobby.getUsers();

        if (!currentUsers.contains(user)) {
            return null;
        }
        return user;
    }


    public Lobby removeUser(Long lobbyId, User userToRemove) {
        Lobby lobby = getLobbyById(lobbyId);

        // Check if the lobby exists
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId);
        }

        // Retrieve the current list of users in the lobby
        List<User> currentUsers = lobby.getUsers();

        // Check if the user to remove is in the lobby
        if (!currentUsers.contains(userToRemove)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in the lobby");
        }

        // Remove the user from the list of users
        currentUsers.remove(userToRemove);

        // Set the updated list of users back to the lobby
        lobby.setUsers(currentUsers);

        if (currentUsers.isEmpty()) {
            deleteLobby(lobbyId);
        } else {
            // Save the updated lobby to the repository
            lobby = lobbyRepository.save(lobby);
            log.debug("Removed user from Lobby: {}", lobby.getId());
        }
        return lobby;
    }

    private void deleteLobby(Long lobbyId) {
        // Retrieve the lobby by ID
        Lobby lobby = getLobbyById(lobbyId);

        // Check if the lobby exists
        if (lobby == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId);
        }

        // Delete the lobby
        lobbyRepository.delete(lobby);

        log.debug("Deleted lobby: {}", lobbyId);
    }
    public void updateUserReadyStatus(Long lobbyId, User userReady) {
        // Find lobby by ID
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));

        Optional<User> optionalUser = lobby.getUsers().stream()
                .filter(u -> u.getId().equals(userReady.getId()))
                .findFirst();
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in lobby");
        }
        // Update user's readiness status
        userReady.setReady(true);
        lobbyRepository.save(lobby);

        }
    public boolean areAllUsersReady(Long lobbyId) {

        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));
        // Get all users in the lobby
        List<User> usersInLobby = getUsersInLobby(lobbyId);

        // Check if all users in the lobby are ready
        return usersInLobby.stream().allMatch(User::isReady); // Assuming there's a method isReady() in the User entity
    }
    public void resetAllUsersReadyStatus(Long lobbyId) {
        // Retrieve the lobby by ID
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));

        // Reset readiness status of all users in the lobby to false
        List<User> users = lobby.getUsers();
        for (User user : users) {
            user.setReady(false);
        }

        // Save the updated lobby
        lobbyRepository.save(lobby);
    }

    public void postMessage(long lobbyId, String message) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));
        lobby.getChat().addMessage(message);
    }

    public ArrayList<String> getMessages(long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));
        return lobby.getChat().getMessages();
    }


}
