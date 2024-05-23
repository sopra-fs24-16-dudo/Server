package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.managers.LobbyManager;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LobbyServiceTest {

    @Mock
    private LobbyManager lobbyManager;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private LobbyService lobbyService;

    private Player testPlayer;
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize test entities
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testPlayer = new Player(testUser);
        testPlayer.setId(1L);

        testLobby = lobbyService.createLobby(testPlayer);
    }

    @Test
    public void createLobby_validInputs_success() {
        Lobby createdLobby = lobbyService.createLobby(testPlayer);

        assertNotNull(createdLobby);
        assertEquals(2L, createdLobby.getId());
        assertEquals(1L, createdLobby.getAdminId());
   }

    @Test
    public void addPlayer_validInputs_success() {
        // Initialize test entities
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testUser");

        Player newPlayer = new Player(testUser2);
        newPlayer.setId(2L);

        lobbyService.addPlayer(1L, newPlayer);

        assertTrue(testLobby.getPlayersList().contains(newPlayer));
    }

    @Test
    public void addPlayer_lobbyFull_throwsException() {
        for (int i = 2; i <= 6; i++) {
            // Initialize test entities
            User testUser2 = new User();
            long longValue = i;
            testUser2.setId(longValue);
            testUser2.setId(longValue);
            testUser2.setUsername("testUser{i}");

            Player player = new Player(testUser2);
            player.setId(longValue);
            testLobby.addPlayer(player);
        }

        User testUser7 = new User();
        testUser7.setId(7L);
        Player extraPlayer = new Player(testUser7);
        extraPlayer.setId(7L);

        assertThrows(ResponseStatusException.class, () -> lobbyService.addPlayer(1L, extraPlayer));
    }

    @Test
    public void getPlayersInGame_validLobbyId_playersReturned() {
        List<Player> players = lobbyService.getPlayersInGame(1L);
        assertEquals(1, players.size());
        assertEquals(testPlayer, players.get(0));
    }

    @Test
    public void getPlayersInLobby_validLobbyId_playersReturned() {
        List<Player> players = lobbyService.getPlayersInLobby(1L);
        assertEquals(1, players.size());
        assertEquals(testPlayer, players.get(0));
    }

    @Test
    public void removePlayer_adminRemoved_newAdminAssigned() {
        // Initialize test entities
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testUser");

        Player newPlayer = new Player(testUser2);
        newPlayer.setId(2L);
        testLobby.addPlayer(newPlayer);

        lobbyService.removePlayer(testLobby, testPlayer.getId());

        assertFalse(testLobby.getPlayersList().contains(testPlayer));
        assertEquals(newPlayer.getId(), testLobby.getAdminId());
    }

    @Test
    public void removePlayer_lastPlayer_lobbyDeleted() {
        lobbyService.removePlayer(testLobby, testPlayer.getId());

        assertFalse(testLobby.getPlayersList().contains(testPlayer));
    }

    @Test
    public void allPlayersReady_allReady_true() {
        testPlayer.setReady(true);

        boolean allReady = lobbyService.allPlayersReady(1L);
        assertTrue(allReady);
    }

    @Test
    public void allPlayersReady_notAllReady_false() {
        testPlayer.setReady(false);

        boolean allReady = lobbyService.allPlayersReady(1L);
        assertFalse(allReady);
    }

    @Test
    public void startGame_gameStarted() {
        lobbyService.startGame(1L);

        assertNotNull(testLobby.getGame());
    }

    /*@Test
    public void startRound_roundStarted() {
        lobbyService.startRound(1L);

        assertNotNull(testLobby.getRound());
    }

    @Test
    public void getRound_roundReturned() {
        Round round = new Round();
        testLobby.setRound(round);

        Round returnedRound = lobbyService.getRound(1L);
        assertEquals(round, returnedRound);
    }*/

    @Test
    public void getRules_rulesReturned() {
        List<String> rules = lobbyService.getRules();
        assertEquals(13, rules.size());
        assertEquals(lobbyService.getRules(), rules);
    }
}
