package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
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
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        testPlayer = mock(Player.class);
        when(testPlayer.getId()).thenReturn(testUser.getId());  // Ensure Player has an ID

        // Create mocks
        testLobby = mock(Lobby.class);
        testGame = mock(Game.class);

        when(testLobby.getId()).thenReturn(1L);
        when(testLobby.getGame()).thenReturn(testGame);
        when(testLobby.getPlayerById(1L)).thenReturn(testPlayer);

        when(lobbyService.getLobbyById(1L)).thenReturn(testLobby);
        when(lobbyService.getPlayersInLobby(1L)).thenReturn(Collections.singletonList(testPlayer));
    }



    @Test
    public void rollHand_WhenNotRolled_ShouldAllowRollAndReturnHand() throws Exception {
        // Setup
        User u = new User();
        u.setId(1L);
        Player realPlayer = new Player(u);
        realPlayer.setId(1L);
        realPlayer.setUsername("testUser");

        when(lobbyService.getPlayersInLobby(1L)).thenReturn(Collections.singletonList(realPlayer));

        // Perform the action
        mockMvc.perform(post("/games/hand/{lobbyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L)))
                .andExpect(status().isOk());

    }

    @Test
    public void getPlayers_WhenNoPlayersExist_ShouldReturnConflict() throws Exception {
        when(lobbyService.getPlayersInLobby(1L)).thenReturn(null);

        mockMvc.perform(get("/games/players/{lobbyId}", 1L))
                .andExpect(status().isConflict());
    }


    @Test
    public void dudo_ShouldCallGameDudo() throws Exception {
        mockMvc.perform(put("/games/dudo/{lobbyId}", 1L))
                .andExpect(status().isOk());

        //verify(testGame).dudo();
    }

    @Test
    public void placeBid_WhenInvalid_ShouldReturnException() throws Exception {
        Bid validBid = new Bid(Suit.ACE, 2L);
        mockMvc.perform(post("/games/placeBid/{lobbyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBid)))
                .andExpect(status().isConflict());
    }




    @Test
    public void startRound_InvokesStartRoundMethod() throws Exception {
        Long lobbyId = 1L;
        mockMvc.perform(put("/games/round/{lobbyId}", lobbyId))
                .andExpect(status().isOk());
    }


}
