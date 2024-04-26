package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Lobby testLobby;
    private Game testGame;
    private User testUser;
    private Player testPlayer;

    @BeforeEach
    public void setup() {
        // Initialize test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        testPlayer = new Player(testUser);

        testLobby = new Lobby();
        testLobby.setId(1L);

        testGame = mock(Game.class);  // Create a mock game object
        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setGame(testGame);  // Set the mock game object in the lobby

        when(lobbyService.getLobbyById(1L)).thenReturn(testLobby);

        // Setup common mock interactions
        when(lobbyService.createLobby(testPlayer)).thenReturn(testLobby);
        when(lobbyService.getLobbyById(1L)).thenReturn(testLobby);
        when(lobbyService.getPlayersInLobby(1L)).thenReturn(Collections.singletonList(testPlayer));
    }


    @Test
    public void getPlayers_ReturnsPlayerList_WhenLobbyExists() throws Exception {
        mockMvc.perform(get("/games/players/{lobbyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    /*@Test
    public void getCurrentBid_ReturnsCurrentBid() throws Exception {
        Bid expectedBid = new Bid("10 JACK");
        testLobby.startGame();
        testLobby.startRound();
        testLobby.getGame().setStartPlayer(testPlayer);
        when(testLobby.getCurrentBid()).thenReturn(expectedBid);

        mockMvc.perform(get("/games/currentBid/{lobbyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedBid.toString()));
    }

    @Test
    public void getNextBid_ReturnsNextBid() throws Exception {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        Player testPlayer = new Player(testUser);
        Lobby lobby = lobbyService.createLobby(testPlayer);

        Bid expectedBid = new Bid("10 JACK");

        when(lobbyService.getLobbyById(lobby.getId())).thenReturn(lobby);
        when(lobby.getNextBid()).thenReturn(expectedBid);

        mockMvc.perform(get("/games/nextBid/{lobbyId}", lobby.getId()))
                .andExpect(status().isOk());
    }*/

    /*@Test
    public void getValidBids_ReturnsValidBids() throws Exception {
        Long lobbyId = 1L;
        String validBids = "10 spades, 11 spades";
        Lobby lobby = new Lobby();
        when(lobbyService.getLobbyById(lobbyId)).thenReturn(lobby);
        when(lobby.getValidBids()).thenReturn(validBids);

        mockMvc.perform(get("/games/validBids/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string(validBids));
    }

    @Test
    public void placeBid_UpdatesBid_WhenCalled() throws Exception {
        Long lobbyId = 1L;
        String bid = "{\"bid\":\"5 spades\"}";
        Lobby lobby = new Lobby();
        when(lobbyService.getLobbyById(lobbyId)).thenReturn(lobby);

        mockMvc.perform(post("/games/placeBid/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bid))
                .andExpect(status().isOk());

        verify(lobbyService).placeBid(eq(lobbyId), any(Bid.class));
    }

    @Test
    public void getCurrentPlayer_ReturnsCurrentPlayerId() throws Exception {
        Long lobbyId = 1L;
        Player currentPlayer = new Player(testUser);

        currentPlayer.setId(2L);

        Lobby createdLobby = lobbyService.createLobby(currentPlayer);
        when(lobbyService.getCurrentPlayer(createdLobby.getId())).thenReturn(currentPlayer);

        mockMvc.perform(get("/games/currentPlayer/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

   @Test
    public void dudo_InvokesDudoMethod() throws Exception {
       Lobby lobby = new Lobby();
       lobby.setId(1L);
        mockMvc.perform(put("/games/dudo/{lobbyId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void getWinner_ReturnsWinner() throws Exception {
        Long lobbyId = 1L;
        Player winner = new Player();
        winner.setId(4L);
        when(lobbyService.getWinner(lobbyId)).thenReturn(winner);

        mockMvc.perform(get("/games/winner/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)));
    }

    @Test
    public void checkWinner_ReturnsTrueIfThereIsAWinner() throws Exception {
        Long lobbyId = 1L;
        when(lobbyService.checkWinner(lobbyId)).thenReturn(true);

        mockMvc.perform(get("/games/winnerCheck/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void getLoser_ReturnsLoser() throws Exception {
        Long lobbyId = 1L;
        Player loser = new Player();
        loser.setId(5L);
        when(lobbyService.getLoser(lobbyId)).thenReturn(loser);

        mockMvc.perform(get("/games/loser/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }*/

    @Test
    public void startRound_InvokesStartRoundMethod() throws Exception {
        Long lobbyId = 1L;
        mockMvc.perform(put("/games/round/{lobbyId}", lobbyId))
                .andExpect(status().isOk());

        //verify(lobbyService).startRound(lobbyId);
    }

    /*@Test
    public void getHands_ReturnsHandsInfo() throws Exception {
        Long lobbyId = 1L;
        String handsInfo = "Player 1: 5 hearts, Player 2: 3 spades";
        when(lobbyService.getHands(lobbyId)).thenReturn(handsInfo);

        mockMvc.perform(get("/games/hands/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string(handsInfo));
    }

    @Test
    public void getSuitCounter_ReturnsSuitCounters() throws Exception {
        Long lobbyId = 1L;
        String suitCounters = "Spades: 5, Hearts: 3";
        when(lobbyService.getSuitCounter(lobbyId)).thenReturn(suitCounters);

        mockMvc.perform(get("/games/counter/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string(suitCounters));
    }*/
}
