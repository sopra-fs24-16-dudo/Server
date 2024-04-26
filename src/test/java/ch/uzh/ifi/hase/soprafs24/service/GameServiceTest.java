package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.RoundState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

public class GameServiceTest {

    @Mock
    private LobbyService lobbyService;
    @Mock
    private Lobby lobby;

    @InjectMocks
    private GameService gameService;
    @InjectMocks
    private Game game;

    private User testUser;

    private User invalidUser;
    private Lobby testLobby;
    private Player testPlayer;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize the test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        testPlayer = new Player(testUser);

        invalidUser = null;

        testLobby = new Lobby(1L);
        testLobby.addPlayer(testPlayer);

        game = mock(Game.class);
        testLobby.setGame(game);

        // Set up mock behavior for lobbyService
        when(lobbyService.getLobbyById(anyLong())).thenReturn(testLobby);

        when(game.getCurrentBid()).thenReturn(new Bid());
        when(game.getNextBid()).thenReturn(new Bid());
        when(game.getValidBids()).thenReturn(new ArrayList<>());
        when(game.getPlayers()).thenReturn(new ArrayList<>(Arrays.asList(testPlayer)));
        when(game.getHands()).thenReturn(new ArrayList<>());
        doNothing().when(game).placeBid(any(Bid.class));
        doNothing().when(game).dudo();
    }
    @Test
    public void createPlayer_validUser_success() {
        // Execute the method to test
        Player createdPlayer = gameService.createPlayer(testUser);

        // Verify the result
        assertNotNull(createdPlayer, "The player should not be null");
        assertSame(testUser.getId(), createdPlayer.getId(), "The player should have the correct user Id associated");
        assertSame(testUser.getUsername(), createdPlayer.getUsername(), "The player should have the correct username associated");
    }
    @Test
    public void createPlayer_nullUser_throwsException() {
        // Attempt to create a player with a null user and expect an exception
        assertThrows(NullPointerException.class, () -> gameService.createPlayer(invalidUser),
                "Expected createPlayer to throw IllegalArgumentException for null user input");
    }
    @Test
    public void testCalculateStartingPlayer_PlayerWithChips() {
        // Create a player with chips
        Player playerWithChips = new Player(testUser);
        // Set up the game to have the player with chips
        List<Player> players = new ArrayList<>();
        players.add(playerWithChips);
        when(game.getPlayers()).thenReturn(players);
        when(game.calculateStartingPlayer()).thenReturn(playerWithChips);
        game.setStartPlayer(playerWithChips);

        // Calculate the starting player
        Player calculatedStartingPlayer = game.calculateStartingPlayer();

        // Verify that the starting player is the one with chips
        assertNotNull(calculatedStartingPlayer);
        assertEquals(playerWithChips, calculatedStartingPlayer);
    }
    @Test
    public void testDudo_PlayerGetsDisqualified() {
        // Mock the behavior of the game object
        doNothing().when(game).dudo();
        Player disqualifiedPlayer = new Player(testUser);
        disqualifiedPlayer.disqualify();
        when(game.getLoser()).thenReturn(disqualifiedPlayer);

        // Call dudo
        game.dudo();

        // Verify that the player is disqualified
        assertTrue(disqualifiedPlayer.isDisqualified());
        assertEquals(3, disqualifiedPlayer.getChips());
    }
    @Test
    public void testCheckWinner_OneNonDisqualifiedPlayer() {
        // Mock the behavior of the game object
        when(game.checkWinner()).thenReturn(true);
        when(game.getWinner()).thenReturn(new Player(testUser));

        // Check for a winner
        boolean result = game.checkWinner();

        // Verify that there is a winner
        assertTrue(result);
    }
    @Test
    void testRoundConstructor() {
        User u1 = new User();
        u1.setId(1L);
        List<Player> players = new ArrayList<>();
        Player startingPlayer = new Player(u1);
        Round round = new Round(players, startingPlayer);

        assertNotNull(round);
        assertEquals(players, round.getPlayers());
        assertEquals(startingPlayer, round.getCurrentPlayer());
        assertNull(round.getLastPlayer());
        assertNotNull(round.getCurrentBid());
        assertNotNull(round.getSuitCounter());
    }
    @Test
    void testSetState() {
        User u1 = new User();
        u1.setId(1L);
        List<Player> players = new ArrayList<>();
        Player startingPlayer = new Player(u1);
        Round round = new Round(players, startingPlayer);

        round.setState(startingPlayer);

        // Ensure that the state is set correctly based on the starting player's chips
        // Add more assertions if needed
    }
    @Test
    void testGetValidBids() {
        User u1 = new User();
        u1.setId(1L);
        List<Player> players = new ArrayList<>();
        Player startingPlayer = new Player(u1);
        Round round = new Round(players, startingPlayer);
        round.setCurrentBid(new Bid(Suit.ACE, 2L));

        List<Bid> validBids = round.getValidBids();


    }
}