package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser1;
    private User testUser2;
    private Player testPlayer1;
    private Player testPlayer2;
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        // Clear existing data before each test
        lobbyService.clearAllLobbies();
        userService.clearAllUsers();

        // Create test users
        testUser1 = createUser("testUser1", "Test User 1");
        testUser2 = createUser("testUser2", "Test User 2");

        // Create players from users
        testPlayer1 = new Player(testUser1);
        testPlayer2 = new Player(testUser2);

        // Create a lobby and add players
        testLobby = lobbyService.createLobby(testPlayer1);
        lobbyService.addPlayer(testLobby.getId(), testPlayer2);

        // Start the game
        lobbyService.startGame(testLobby.getId());
        lobbyService.startRound(testLobby.getId());

        // Roll hands for both players
        Hand hand1 = new Hand();
        hand1.roll();
        testPlayer1.setHand(hand1);

        Hand hand2 = new Hand();
        hand2.roll();
        testPlayer2.setHand(hand2);
    }

    private User createUser(String username, String name) {
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setToken(UUID.randomUUID().toString());
        return userService.createUser(user);
    }

    private void rollHand(Long userId) {
        try {
            mockMvc.perform(post("/games/hand/" + testLobby.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userId)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPlayers_validLobbyId_playersReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/players/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(Math.toIntExact(testPlayer1.getId()))))
                .andExpect(jsonPath("$[1].id", is(Math.toIntExact(testPlayer2.getId()))));
    }

    @Test
    public void rollHand_validLobbyAndUserId_handRolled() throws Exception {
        // when/then
        mockMvc.perform(post("/games/hand/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser1.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dices", hasSize(5)));
    }

    @Test
    public void getBid_validLobbyId_currentBidReturned() throws Exception {
        // Place a bid first
        testLobby.placeBid(new Bid(Suit.NINE, 3L));

        // when/then
        mockMvc.perform(get("/games/currentBid/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("3 NINE")));
    }

    @Test
    public void getNextBid_validLobbyId_nextBidReturned() throws Exception {
        // Place a bid first
        testLobby.placeBid(new Bid(Suit.NINE, 3L));

        // when/then
        mockMvc.perform(get("/games/nextBid/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getValidBids_validLobbyId_validBidsReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/validBids/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void placeBid_validLobbyId_bidPlaced() throws Exception {
        testLobby.placeBid(new Bid(Suit.NINE, 2L)); // Initial bid to ensure subsequent bid is valid

        // when/then
        mockMvc.perform(post("/games/placeBid/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("3 NINE")))
                .andExpect(status().isOk());
    }

    @Test
    public void getCurrentPlayer_validLobbyId_currentPlayerReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/currentPlayer/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is((int) testPlayer1.getId())));
    }

    @Test
    public void getLastPlayer_validLobbyId_lastPlayerReturned() throws Exception {
        // Set the last player
        testLobby.getRound().setLastPlayer(testPlayer1);

        // when/then
        mockMvc.perform(get("/games/lastPlayer/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is((int) testPlayer1.getId())));
    }

    @Test
    public void dudo_validLobbyId_dudoCalled() throws Exception {
        // Place a bid first to avoid null totalAmount
        testLobby.placeBid(new Bid(Suit.NINE, 3L));

        // when/then
        mockMvc.perform(put("/games/dudo/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getWinner_validLobbyId_winnerReturned() throws Exception {
        // Set a winner first
        testLobby.setWinner(testPlayer1);

        // when/then
        mockMvc.perform(get("/games/winner/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Math.toIntExact(testPlayer1.getId()))));
    }

    @Test
    public void checkWinner_validLobbyId_winnerCheckReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/winnerCheck/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    public void getLoser_validLobbyId_loserReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/loser/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void startRound_validLobbyId_roundStarted() throws Exception {
        // when/then
        mockMvc.perform(put("/games/round/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getSuitCounter_validLobbyId_suitCounterReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/counter/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /*@Test
    public void getHands_validLobbyId_handsReturned() throws Exception {
        mockMvc.perform(post("/games/hand/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser1.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dices", hasSize(5)));
        mockMvc.perform(post("/games/hand/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dices", hasSize(5)));
        // when/then
        mockMvc.perform(get("/games/hands/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/

    @Test
    public void endGame_validLobbyId_gameEnded() throws Exception {
        // when/then
        mockMvc.perform(put("/games/end/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkFijo_validLobbyId_fijoCheckReturned() throws Exception {
        // when/then
        mockMvc.perform(get("/games/fijoCheck/" + testLobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }
}
