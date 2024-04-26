package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.managers.LobbyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LobbyServiceTest {

    @Mock
    private LobbyManager lobbyManager;

    @InjectMocks
    private LobbyService lobbyService;

    private Player testPlayer;
    private Lobby testLobby;
    private Map<Long, Lobby> lobbies;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Given a test player and lobby
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testPlayer = new Player(testUser);
        testLobby = new Lobby(1L);
        testLobby.addPlayer(testPlayer);

        when(lobbyManager.generateLobbyId()).thenReturn(1L);
        when(lobbyManager.getLobby(1L)).thenReturn(testLobby);
        doAnswer(invocation -> {
            Lobby lobby = invocation.getArgument(0);
            lobbies.put(lobby.getId(), lobby);
            return null;
        }).when(lobbyManager).addLobby(any(Lobby.class));
    }

    @Test
    public void createLobby_validInputs_success() {
        System.out.println("Testing with player: " + testPlayer);
        Lobby createdLobby = lobbyService.createLobby(testPlayer);

        assertNotNull(createdLobby);
        assertEquals(1L, createdLobby.getId());
        assertTrue(createdLobby.getPlayers().containsValue(testPlayer));
        //verify(lobbyManager, times(1)).addLobby(any(Lobby.class));
    }

    @Test
    public void addPlayer_toExistingLobby_success() {
        Lobby createdLobby = lobbyService.createLobby(testPlayer);
        System.out.println(testLobby.getId());

        lobbyService.addPlayer(createdLobby.getId(), testPlayer);

        assertTrue(createdLobby.getPlayers().containsValue(testPlayer));
        //verify(lobbyManager, times(1)).getLobby(1L);
    }

    @Test
    public void addPlayer_toFullLobby_throwsException() {
        when(lobbyManager.getLobby(1L)).thenReturn(testLobby);
        Lobby testLobby = lobbyService.createLobby(testPlayer);
        for (int i = 0; i < 6; i++) {
            User u = new User();
            u.setId(i + 1L);
            testLobby.addPlayer(new Player(u));
        }

        Exception exception = assertThrows(ResponseStatusException.class, () -> lobbyService.addPlayer(1L, testPlayer));
        assertEquals("400 BAD_REQUEST \"Lobby is full\"", exception.getMessage());
    }

    @Test
    public void removePlayer_fromLobby_success() {
        Lobby testLobby = lobbyService.createLobby(testPlayer);
        testLobby.addPlayer(testPlayer);

        lobbyService.removePlayer(testLobby, testPlayer.getId());

        assertFalse(testLobby.getPlayers().containsValue(testPlayer));
        //verify(lobbyManager, times(1)).removeLobby(testLobby.getId());
    }

    @Test
    public void allPlayersReady_allReady_returnsTrue() {
        Lobby testLobby = lobbyService.createLobby(testPlayer);

        testPlayer.setReady(true);
        testLobby.addPlayer(testPlayer);

        assertTrue(lobbyService.allPlayersReady(1L));
    }

    @Test
    public void postMessage_addsMessageToChat() {
        Lobby testLobby = lobbyService.createLobby(testPlayer);

        String message = "Hello, World!";
        lobbyService.postMessage(1L, message);

        assertEquals(1, testLobby.getChat().getMessages().size());
        assertTrue(testLobby.getChat().getMessages().contains(message));
    }

    @Test
    public void startGame_startsGameWithinLobby() {
        Lobby testLobby = lobbyService.createLobby(testPlayer);

        lobbyService.startGame(1L);
        assertNotNull(testLobby.getGame());
    }
}