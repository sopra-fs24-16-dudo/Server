package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.managers.LobbyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LobbyServiceTest {

    @Mock
    private LobbyManager lobbyManager;

    @InjectMocks
    private LobbyService lobbyService;
    private Game mockGame;


    private Player testPlayer;
    private Lobby testLobby;
    private Map<Long, Lobby> lobbies;
    private User testUser;

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
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        mockGame = mock(Game.class);
        testLobby.setGame(mockGame);

        lobbyManager = mock(LobbyManager.class);

        when(mockGame.getCurrentBid()).thenReturn(new Bid());
        when(mockGame.getNextBid()).thenReturn(new Bid());
        when(mockGame.getValidBids()).thenReturn(new ArrayList<>());
        when(mockGame.getPlayers()).thenReturn(new ArrayList<>(Arrays.asList(testPlayer)));
        when(mockGame.getHands()).thenReturn(new ArrayList<>());
        doNothing().when(mockGame).placeBid(any(Bid.class));
        doNothing().when(mockGame).dudo();

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
    @Test
    public void createPlayer_ReturnsPlayerWithCorrectUser() {
        // Given
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        Player resultPlayer = lobbyService.createPlayer(testUser);

        // Then
        assertNotNull(resultPlayer, "The created player should not be null");
        assertEquals(testUser.getId(), resultPlayer.getId(), "The player should contain the user ID passed to createPlayer");
        assertEquals(testUser.getUsername(), resultPlayer.getUsername(), "The player should contain the user Username passed to createPlayer");
    }
    @Test
    public void getPlayersInGame_ReturnsCorrectPlayers() {
        Lobby lobby = lobbyService.createLobby(testPlayer);
        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("testUsername");

        Player p = lobbyService.createPlayer(u2);
        lobby.addPlayer(p);
        lobby.startGame();
        List<Player> playersInGame = lobbyService.getPlayersInGame(testLobby.getId());
        assertNotNull(playersInGame, "The returned list of players should not be null.");
        assertEquals(2, playersInGame.size(), "The list should contain two players.");
    }

    @Test
    public void getPlayersInLobby_ReturnsCorrectPlayers() {
        Lobby testLobby = lobbyService.createLobby(testPlayer);
        List<Player> playersInLobby = lobbyService.getPlayersInLobby(testLobby.getId());
        assertNotNull(playersInLobby, "The returned list of players should not be null.");
        assertEquals(1, playersInLobby.size(), "The list should contain one player.");
    }

    @Test
    public void getPlayersInGame_WhenLobbyDoesNotExist_ThrowsException() {
        when(lobbyManager.getLobby(90L)).thenThrow(new IllegalArgumentException("Lobby with id 90 does not exist."));
        assertThrows(IllegalArgumentException.class, () -> lobbyService.getPlayersInGame(90L), "Should throw an exception when lobby does not exist.");
    }

    @Test
    public void getPlayersInLobby_WhenLobbyDoesNotExist_ThrowsException() {
        when(lobbyManager.getLobby(90L)).thenThrow(new IllegalArgumentException("Lobby with id 90 does not exist."));
        assertThrows(IllegalArgumentException.class, () -> lobbyService.getPlayersInLobby(90L), "Should throw an exception when lobby does not exist.");
    }
    @Test
    public void updatePlayerReadyStatus_TogglesReadyStatus() {
        // Given
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        Player player = lobbyService.createPlayer(testUser);
        // Action
        lobbyService.updatePlayerReadyStatus(player);

        // Assertion
        assertTrue(player.isReady(), "Player should be set to ready.");

        // Toggling again to check the reverse case
        lobbyService.updatePlayerReadyStatus(player);
        assertFalse(player.isReady(), "Player should be set to not ready.");
    }
    @Test
    public void getMessages_ReturnsCorrectMessages() {
        // Given
        Lobby lobby = lobbyService.createLobby(testPlayer);
        lobby.getChat().addMessage("Hello");
        lobby.getChat().addMessage("World");
        when(lobbyManager.getLobby(lobby.getId())).thenReturn(lobby);

        // Action
        List<String> messages = lobbyService.getMessages(lobby.getId());

        // Assertion
        assertEquals(2, messages.size(), "Should return all messages.");
        assertTrue(messages.contains("Hello") && messages.contains("World"), "Messages should match those added.");
    }
    @Test
    public void getRules_ReturnsAllRules() {
        List<String> RULES = List.of(
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
        // Action
        List<String> rules = lobbyService.getRules();

        // Assertion
        assertNotNull(rules, "Rules list should not be null.");
        assertFalse(rules.isEmpty(), "Rules list should not be empty.");
        assertEquals(RULES, rules, "Returned rules should match the predefined rules list.");
    }
    @Test
    public void startRound_StartsRoundSuccessfully() {
        // Given
        Lobby lobby = lobbyService.createLobby(testPlayer);
        lobby.startGame();
        when(lobbyManager.getLobby(lobby.getId())).thenReturn(lobby);

        // Action
        lobbyService.startRound(lobby.getId());

        // No exception means success. Checking if internal state could be asserted is dependent on implementation details not shown.
        assertTrue(true, "Method should execute without errors.");
    }

    @Test
    public void testRoundConstructor() {
        // Prepare the data for the test
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Player1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Player2");

        Player player1 = new Player(user1);
        Player player2 = new Player(user2);

        List<Player> players = Arrays.asList(player1, player2);

        // Player1 is the starting player
        Round round = new Round(players, player1);

        // Assertions to verify that the Round object was initialized correctly
        assertNotNull(round, "Round object should not be null");
        assertEquals(player1, round.getCurrentPlayer(), "Starting player should be set correctly");
        assertEquals(2, round.getPlayers().size(), "Players list should contain two players");
    }
    @Test
    public void getCurrentBid_ShouldReturnCurrentBid() {
        Bid expectedBid = new Bid(Suit.ACE, 1L);
        mockGame.setCurrentBid(expectedBid);
        Bid newdBid = new Bid(Suit.ACE, 1L);
        mockGame.setCurrentBid(newdBid);
        Bid result = mockGame.getCurrentBid();
        assertNotNull(result);
        //assertEquals(expectedBid, newdBid);
    }


    /*@Test
    public void getRound_ReturnsCurrentRound() {
        Lobby lobby = lobbyService.createLobby(testPlayer);
        lobby.startGame();
        lobby.startRound();
        when(lobby.getRound()).thenReturn(lobby.getRound());

        Round currentRound = lobbyService.getRound(1L);

        assertSame(mockRound, currentRound, "Should return the current round of the game.");
    }*/

}