package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class LobbyService {

    private static final List<String> RULES = List.of(
        "Rule 1: Your Resources - You start with 5 dice and 2 chips.",
        "Rule 2: Rolling the Dice - Every round begins with all players rolling their dice simultaneously.",
        "Rule 3: Starting Player - The starting player is chosen randomly.",
        "Rule 4: Your Turn - You have the chance to make the first bid.",
        "Rule 5: Making a Bid - When it's your turn, announce the minimum number of dice showing a certain suit (e.g., 'five queens').",
        "Rule 6: Raising the Stakes - You can raise the bid by increasing the quantity of dice, the die number, or both.",
        "Rule 7: Wild Aces - Aces act as wild cards, except when you're in a 'Fijo' state.",
        "Rule 8: Bidding with Aces - If you wish to bid aces, halve the quantity of dice, round down, and add one.",
        "Rule 9: Challenging a Bid - If you don't believe the previous bid is correct, call 'dudo' to challenge it.",
        "Rule 10: Consequences - If your challenge fails, you lose a chip; if it succeeds, your opponent loses a chip.",
        "Rule 11: Fijo Round - When you reach zero chips, a 'Fijo' round begins, where aces are not wild.",
        "Rule 12: Elimination - Lose a round with zero chips, and you're out of the game.",
        "Rule 13: Victory - Be the last player standing to win the game: If you have two chips left, earn two points; if you have one or no chips left, earn one point."
    );

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    private UserService userService;
    private VoiceChannelService voiceChannelService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserService userService, VoiceChannelService voiceChannelService) {
        this.lobbyRepository = lobbyRepository;
        this.userService = userService;
        this.voiceChannelService = voiceChannelService;
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

        VoiceChannel voiceChannel = voiceChannelService.createVoiceChannel(newLobby);
        // Associate the voice channel with the lobby
        newLobby.setVoiceChannel(voiceChannel);

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

        updatedlobby.addUser(newUser);
       
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

    public List<String> getMessages(long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));
        return lobby.getChat().getMessages();
    }

    public List<String> getRules() {
        return RULES;
    }

    public void startGame(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));
        lobby.startGame();
    }

}
