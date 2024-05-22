package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.managers.LobbyManager;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
//import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;


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

    private final LobbyManager lobbyManager;

    private final SimpMessagingTemplate messagingTemplate;


    public LobbyService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyManager = new LobbyManager();
    }

    public Lobby getLobbyById(Long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return lobby;
    }

    public List<Lobby> getAllLobbies() {
        return lobbyManager.getLobbies();
    }

    public Lobby createLobby(Player newPlayer) {
        Long id = lobbyManager.generateLobbyId();
        Lobby newLobby = new Lobby(id);
        newLobby.setAdminId(newPlayer.getId());
        newLobby.addPlayer(newPlayer);
        lobbyManager.addLobby(newLobby);
        log.debug("Created Information for Lobby: {}", newLobby.getId());
        return newLobby;
    }

    public Player createPlayer (User user){
        return new Player(user);
    }

    public Lobby addPlayer(Long lobbyId, Player player) {
        Lobby updatedLobby = lobbyManager.getLobby(lobbyId);
        if (playerInLobby(lobbyId, player)) {
            return updatedLobby;
        }
        checkIfLobbyFull(lobbyId);
        updatedLobby.addPlayer(player);
        log.debug("Added user to Lobby: {}", updatedLobby.getId());
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(updatedLobby));
        return updatedLobby;
    }

    //check if lobby with id is full (max. 6 users)
    private void checkIfLobbyFull(Long lobbyId){
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        if (lobby.players.size() >= 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby is full");
        }
    }

    public List<Player> getPlayersInGame(Long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return lobby.getPlayersInGame();
    }

    public List<Player> getPlayersInLobby(Long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return lobby.getPlayersList();
    }

    public boolean playerInLobby(Long lobbyId, Player player) {

        Lobby lobby = getLobbyById(lobbyId);

        if (!lobby.getPlayers().containsValue(player)) {
            return false;
        }
        return true;
    }


    public Lobby removePlayer(Lobby lobby, long playerId) {
        lobby.deletePlayer(playerId);
        if (lobby.getPlayersList().isEmpty()) {
            deleteLobby(lobby.getId());
            return null;
        }
        if (playerId == (lobby.getAdminId())) {
            lobby.setAdminId(lobby.getPlayersList().stream()
                    .filter(player -> !Long.valueOf(player.getId()).equals(playerId))
                    .findFirst()
                    .map(Player::getId)
                    .orElse(null));
        }
        messagingTemplate.convertAndSend("/topic/lobby/" + lobby.getId(), DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
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
        lobbyManager.removeLobby(lobbyId);

        log.debug("Deleted lobby: {}", lobbyId);
    }
    public void updatePlayerReadyStatus(Player player) {
        // Update user's readiness status
        if (player.isReady()) {
            player.setReady(false);
        } else {
            player.setReady(true);
        }
    }
    public boolean allPlayersReady(Long lobbyId) {

        Lobby lobby = lobbyManager.getLobby(lobbyId);
        // Get all users in the lobby
        List<Player> playersInLobby = lobby.getPlayersList();

        for (Player player : playersInLobby) {
            if (!player.isReady()) {
                return false;
            }
        }return true;
    }
    //public void resetAllUsersReadyStatus(Long lobbyId) {
    //    // Retrieve the lobby by ID
    //    Lobby lobby = lobbyRepository.findById(lobbyId)
    //            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found with id: " + lobbyId));

        // Reset readiness status of all users in the lobby to false
    //    List<User> users = lobby.getUsers();
    //    for (User user : users) {
    //        user.setReady(false);
    //    }

        // Save the updated lobby
    //    lobbyRepository.save(lobby);
    //}

    public void postMessage(long lobbyId, String message) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        lobby.getChat().addMessage(message);
    }

    public List<String> getMessages(long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return lobby.getChat().getMessages();
    }

    public List<String> getRules() {
        return RULES;
    }

    public void startGame(Long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        lobby.startGame();
    }

    public void startRound(Long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        lobby.startRound();
    }

    public Round getRound(Long lobbyId) {
        Lobby lobby = lobbyManager.getLobby(lobbyId);
        return lobby.getRound();
    }
    /*public void updateLobby(Lobby updatedLobby) {
        Long lobbyId = updatedLobby.getId();
        if (!lobbyManager.getLobbies().contains(updatedLobby)) {
            throw new IllegalArgumentException("Lobby with id " + lobbyId + " does not exist.");
        }

        lobbyManager.updateLobby(updatedLobby, lobbyId);
    }

     */

    public Leaderboard getLeaderboard(Lobby lobby) {
        return lobby.getLeaderboard();
    }

    // Methods for testing
    public void clearAllLobbies() {
        lobbyManager.clearAllLobbies();
    }

    public void saveLobby(Lobby lobby) {
        lobbyManager.addLobby(lobby);
    }


}
