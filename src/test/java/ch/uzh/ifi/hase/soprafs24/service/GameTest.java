package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.RoundState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    @Mock
    private Lobby mockLobby;
    @Mock
    private Player mockPlayer1;
    @Mock
    private Player mockPlayer2;
    @Mock
    private Player mockPlayer3;
    @Mock
    private Round mockRound;

    @InjectMocks
    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockLobby.getId()).thenReturn(1L);

        LinkedHashMap<Long, Player> players = new LinkedHashMap<>();
        players.put(1L, mockPlayer1);
        players.put(2L, mockPlayer2);
        players.put(3L, mockPlayer3);

        when(mockLobby.getPlayers()).thenReturn(players);
        when(mockPlayer1.getId()).thenReturn(1L);
        when(mockPlayer2.getId()).thenReturn(2L);
        when(mockPlayer3.getId()).thenReturn(3L);

        game = new Game(mockLobby);
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game);
        assertEquals(3, game.getPlayers().size());
        assertNull(game.getWinner());
    }

    @Test
    void testCheckWinner_NoWinner() {
        when(mockPlayer1.isDisqualified()).thenReturn(false);
        when(mockPlayer2.isDisqualified()).thenReturn(false);
        when(mockPlayer3.isDisqualified()).thenReturn(false);

        assertFalse(game.checkWinner());
    }

    @Test
    void testCheckWinner_WithWinner() {
        when(mockPlayer1.isDisqualified()).thenReturn(true);
        when(mockPlayer2.isDisqualified()).thenReturn(true);
        when(mockPlayer3.isDisqualified()).thenReturn(false);

        assertTrue(game.checkWinner());
        assertEquals(mockPlayer3, game.getWinner());
    }

    @Test
    void testCalculateStartingPlayer_FirstRound() {
        when(mockPlayer1.getChips()).thenReturn(2);

        Player startingPlayer = game.calculateStartingPlayer();
        assertNotNull(startingPlayer);
        assertEquals(mockPlayer1, startingPlayer);
    }

    @Test
    void testGetPlayers() {
        List<Player> players = game.getPlayers();
        assertEquals(3, players.size());
        assertTrue(players.contains(mockPlayer1));
        assertTrue(players.contains(mockPlayer2));
        assertTrue(players.contains(mockPlayer3));
    }

    @Test
    void testSetPlayers() {
        LinkedHashMap<Long, Player> newPlayers = new LinkedHashMap<>();
        Player newPlayer = mock(Player.class);
        when(newPlayer.getId()).thenReturn(4L);
        newPlayers.put(4L, newPlayer);

        game.setPlayers(newPlayers);

        assertEquals(1, game.getPlayers().size());
        assertEquals(newPlayer, game.getPlayers().get(0));
    }
}
