package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    public void getPlayers_ReturnsPlayerList_WhenLobbyExists() throws Exception {
        Long lobbyId = 1L;
        User u1 = new User();
        u1.setId(2L);
        Player player = new Player(u1);
        List<Player> players = Collections.singletonList(new Player(u1));
        when(lobbyService.getPlayersInLobby(lobbyId)).thenReturn(players);

        mockMvc.perform(get("/games/players/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void rollHand_ReturnsHand_WhenPlayerHasNotRolledYet() throws Exception {
        Long lobbyId = 1L, userId = 2L;
        Lobby lobby = mock(Lobby.class); // Ensure the lobby is a mock
        Player player = mock(Player.class); // Ensure the player is also a mock
        Hand hand = new Hand(); // Assuming Hand has a no-arg constructor

        // Setup mocks
        when(lobbyService.getLobbyById(lobbyId)).thenReturn(lobby);
        when(lobby.getPlayerById(userId)).thenReturn(player);
        when(player.hasRolled()).thenReturn(false);
        when(player.getHand()).thenReturn(hand);

        // Perform the action
        mockMvc.perform(post("/games/hand/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId)))
                .andExpect(status().isOk());

        // Verify the behaviors
        //verify(player).roll();  // Ensure roll() was called since player has not rolled yet
    }

    /*@Test
    public void getCurrentBid_ReturnsCurrentBid() throws Exception {
        Long lobbyId = 1L;
        String expectedBid = "10 spades";
        Lobby lobby = new Lobby();
        when(lobbyService.getLobbyById(lobbyId)).thenReturn(lobby);
        when(lobby.getCurrentBid()).thenReturn(expectedBid);

        mockMvc.perform(get("/games/currentBid/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedBid));
    }

    @Test
    public void getNextBid_ReturnsNextBid() throws Exception {
        Long lobbyId = 1L;
        String expectedBid = "11 spades";
        Lobby lobby = new Lobby();
        when(lobbyService.getLobbyById(lobbyId)).thenReturn(lobby);
        when(lobby.getNextBid()).thenReturn(expectedBid);

        mockMvc.perform(get("/games/nextBid/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedBid));
    }

    @Test
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
        Player currentPlayer = new Player();
        currentPlayer.setId(2L);
        when(lobbyService.getCurrentPlayer(lobbyId)).thenReturn(currentPlayer);

        mockMvc.perform(get("/games/currentPlayer/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void dudo_InvokesDudoMethod() throws Exception {
        Long lobbyId = 1L;
        mockMvc.perform(put("/games/dudo/{lobbyId}", lobbyId))
                .andExpect(status().isOk());

        verify(lobbyService).dudo(lobbyId);
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
    }

    @Test
    public void startRound_InvokesStartRoundMethod() throws Exception {
        Long lobbyId = 1L;
        mockMvc.perform(put("/games/round/{lobbyId}", lobbyId))
                .andExpect(status().isOk());

        verify(lobbyService).startRound(lobbyId);
    }

    @Test
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
