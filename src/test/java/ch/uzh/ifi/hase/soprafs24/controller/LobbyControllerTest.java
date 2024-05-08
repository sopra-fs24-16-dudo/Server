package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    // ObjectMapper for JSON conversions
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenLobbies_whenGetAllLobbies_thenReturnJsonArray() throws Exception {
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        List<Lobby> allLobbies = Arrays.asList(lobby);
        List<LobbyGetDTO> lobbyGetDTOs = Collections.singletonList(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));

        given(lobbyService.getAllLobbies()).willReturn(allLobbies);

        mockMvc.perform(get("/lobbies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) lobby.getId())));
    }

    @Test
    public void whenCreateLobby_withValidUser_thenCreateLobby() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Player player = new Player(user);
        Lobby lobby = new Lobby();
        lobby.setId(1L);

        // Mock the retrieval of the User.
        given(userService.getUserById(userId)).willReturn(user);

        // Mock the creation of a Player from a User.
        given(lobbyService.createPlayer(user)).willReturn(player);

        // Ensure the lobbyService.createLobby is called with the mocked Player.
        given(lobbyService.createLobby(player)).willReturn(lobby);

        // Mock the API call
        mockMvc.perform(post("/lobbies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId))) // Sending ID as JSON.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is((int)lobby.getId()))) // Ensure the response has the lobby ID.
                .andDo(print()); // Print the response for debugging.
    }


    //REVISED TESTING METHOD
    //Test: Listing All Lobbies
    @Test
    public void whenGetAllLobbies_thenReturnLobbiesList() throws Exception {
        // Given
        Lobby lobby1 = new Lobby(1L);
        Lobby lobby2 = new Lobby(2L);
        List<Lobby> lobbies = List.of(lobby1, lobby2);
        given(lobbyService.getAllLobbies()).willReturn(lobbies);

        // When & Then
        mockMvc.perform(get("/lobbies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
    //Test: Create a New Lobby
    @Test
    public void whenCreateLobby_thenReturnLobby() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        Player player = new Player(user);
        Lobby lobby = new Lobby(3L);

        given(userService.getUserById(Mockito.any())).willReturn(user);
        given(lobbyService.createPlayer(Mockito.any(User.class))).willReturn(player);
        given(lobbyService.createLobby(Mockito.any(Player.class))).willReturn(lobby);

        // When & Then
        mockMvc.perform(post("/lobbies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L))) // Serialize the input as JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3))); // Ensure the ID is returned as expected
    }
    //Test: Add Player to Lobby
    @Test
    public void whenAddPlayerToLobby_ifPlayerNotInLobby_thenAddPlayer() throws Exception {
        // Given
        User user = new User();
        user.setId(2L);
        Player player = new Player(user);

        given(userService.getUserById(Mockito.anyLong())).willReturn(user);
        given(lobbyService.createPlayer(Mockito.any(User.class))).willReturn(player);
        given(lobbyService.playerInLobby(Mockito.anyLong(), Mockito.any(Player.class))).willReturn(false);
        given(lobbyService.addPlayer(Mockito.anyLong(), Mockito.any(Player.class))).willReturn(new Lobby(1L));

        // When & Then
        mockMvc.perform(put("/lobby/players/{lobbyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(2L)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenAddPlayerToLobby_ifPlayerAlreadyInLobby_thenThrowException() throws Exception {
        // Given
        User user = new User();
        user.setId(2L);
        Player player = new Player(user);

        given(userService.getUserById(Mockito.anyLong())).willReturn(user);
        given(lobbyService.createPlayer(Mockito.any(User.class))).willReturn(player);
        given(lobbyService.playerInLobby(Mockito.anyLong(), Mockito.any(Player.class))).willReturn(true);

        // When & Then
        mockMvc.perform(put("/lobby/players/{lobbyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(2L)))
                .andExpect(status().isConflict());
    }

    //Test: Remove Player from Lobby
    @Test
    public void whenRemovePlayerFromLobby_thenRemovePlayer() throws Exception {
        // Given
        Lobby lobby = new Lobby(1L);
        User u1 = new User();
        User u2 = new User();
        u1.setId(3L);
        u2.setId(4L);
        Player p1 = new Player(u1);
        Player p2 = new Player(u2);
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);

        given(lobbyService.getLobbyById(Mockito.anyLong())).willReturn(lobby);
        given(lobbyService.removePlayer(Mockito.any(Lobby.class), Mockito.anyLong())).willReturn(lobby);

        // When & Then
        mockMvc.perform(post("/lobby/exit/{lobbyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRemovePlayerFromLobby_ifLobbyNotFound_thenReturnNotFound() throws Exception {
        // Given
        given(lobbyService.getLobbyById(Mockito.anyLong())).willThrow(new IllegalArgumentException("Lobby not found"));

        // When & Then
        mockMvc.perform(post("/lobby/exit/{lobbyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isConflict());
    }
    @Test
    public void whenUpdateUserReadyStatus_thenUpdateStatus() throws Exception {
        // Given
        Lobby lobby = new Lobby(1L);
        User u1 = new User();
        u1.setId(4L);
        Player player = new Player(u1);
        lobby.addPlayer(player);

        given(lobbyService.getLobbyById(Mockito.anyLong())).willReturn(lobby);
        doNothing().when(lobbyService).updatePlayerReadyStatus(Mockito.any(Player.class));

        // When & Then
        mockMvc.perform(put("/lobby/player/{lobbyId}/ready", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("4"))
                .andExpect(status().isOk());
    }
    @Test
    public void whenAreAllPlayersReady_thenReturnStatus() throws Exception {
        User u1 = new User();
        u1.setId(1L);
        Player p1 = new Player(u1);
        p1.setReady(true);

        User u2 = new User();
        u2.setId(2L);
        Player p2 = new Player(u2);
        p2.setReady(true);
        // Given
        given(lobbyService.allPlayersReady(Mockito.anyLong())).willReturn(true);
        given(lobbyService.getPlayersInLobby(Mockito.anyLong())).willReturn(List.of(p1, p2));

        // When & Then
        mockMvc.perform(get("/lobby/player/{lobbyId}/ready", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }
}

 */