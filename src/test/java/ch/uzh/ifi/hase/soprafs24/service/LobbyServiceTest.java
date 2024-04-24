package ch.uzh.ifi.hase.soprafs24.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.managers.LobbyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LobbyServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private LobbyManager lobbyManager;

    @InjectMocks
    private LobbyService lobbyService;

    private User testUser;
    private Player testPlayer;
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L); // Set a default ID
        testUser.setName("testName");
        testUser.setUsername("testUsername");

        testPlayer = new Player(testUser);
        testPlayer.setId(testUser.getId()); // Ensure ID consistency

        testLobby = new Lobby(999L);
        testLobby.addPlayer(testPlayer);

        when(lobbyManager.getLobby(anyLong())).thenReturn(testLobby);
        when(lobbyManager.generateLobbyId()).thenReturn(1L);
    }

    /*@Test
    public void createLobby_withNewPlayer_success() {
        Player newPlayer = lobbyService.createPlayer(testUser);
        assertNotNull(newPlayer);

        Lobby createdLobby = lobbyService.createLobby(newPlayer);
        assertNotNull(createdLobby);
        assertTrue(createdLobby.getPlayersList().contains(newPlayer));
        verify(lobbyManager, times(1)).addLobby(any(Lobby.class));
    }

    @Test
    public void addPlayerToLobby_success() {
        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("Another User");

        Player anotherPlayer = new Player(newUser);
        anotherPlayer.setId(3L); // Simulate new player creation with unique ID

        Lobby updatedLobby = lobbyService.addPlayer(testLobby.getId(), anotherPlayer);
        assertNotNull(updatedLobby);
        assertTrue(updatedLobby.getPlayersList().contains(anotherPlayer));
    }

    @Test
    public void addPlayerToFullLobby_throwsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            User newUser = new User();
            newUser.setId(7L);
            newUser.setName("LaterUser");

            Player anotherPlayer = new Player(newUser);
            anotherPlayer.setId(7L);
            lobbyService.addPlayer(testLobby.getId(), anotherPlayer);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void removePlayerFromLobby_updatesLobbyCorrectly() {
        long playerIdToRemove = testPlayer.getId();
        lobbyService.removePlayer(testLobby.getId(), playerIdToRemove);
        assertTrue(testLobby.getPlayersList().isEmpty());
    }

    @Test
    public void getAllLobbies_returnsAllLobbies() {
        when(lobbyManager.getLobbies()).thenReturn(List.of(testLobby));

        List<Lobby> lobbies = lobbyService.getAllLobbies();
        assertFalse(lobbies.isEmpty());
        assertEquals(1, lobbies.size());
    }

    @Test
    public void getLobbyById_validId_returnsLobby() {
        Lobby foundLobby = lobbyService.getLobbyById(testLobby.getId());
        assertEquals(foundLobby, testLobby);
    }

    @Test
    public void startGame_checksAllUsersReady() {
        testPlayer.setReady(true);
        testLobby.startGame(); // simulate game start

        assertTrue(lobbyService.allPlayersReady(testLobby.getId()));
    }*/
}