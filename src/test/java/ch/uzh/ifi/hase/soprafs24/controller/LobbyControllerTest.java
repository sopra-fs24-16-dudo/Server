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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

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
                .andExpect(jsonPath("$[0].id", is((int) lobby.getId())))
                .andDo(print());
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

    /*
    @Test
    public void whenAddUserToLobby_withExistingUser_thenLobbyIsUpdated() throws Exception {
        Long userId = 1L;
        Long lobbyId = 1L;

        User userToAdd = new User();
        userToAdd.setId(userId);
        Lobby initialLobby = new Lobby();
        initialLobby.setId(lobbyId);
        initialLobby.addPlayer(new ArrayList<>());  // Ensure the users list is initialized

        Lobby updatedLobby = new Lobby();
        updatedLobby.setId(lobbyId);
        updatedLobby.setUsers(new ArrayList<>());  // Copy existing users
        updatedLobby.getUsers().add(userToAdd);  // Add new user

        given(userService.getUserById(userId)).willReturn(userToAdd);
        given(lobbyService.addUser(lobbyId, userToAdd)).willReturn(updatedLobby);

        mockMvc.perform(put("/lobby/user/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(userId)))
                .andExpect(status().isNoContent());

        verify(lobbyService).addUser(lobbyId, userToAdd);  // Verify that addUser was indeed called with the correct parameters

        // Additional verification logic is on service layer tests(actually new user was added to the lobby etc.
    }

    @Test
    public void whenAddUserToLobby_withNonExistingUser_thenNotFound() throws Exception {
        Long userId = 999L;
        Long lobbyId = 1L;

        given(userService.getUserById(userId)).willReturn(null);

        mockMvc.perform(put("/lobby/user/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetUsersInLobby_withValidLobbyId_thenReturnUsers() throws Exception {
        Long lobbyId = 1L;  // Example lobby ID

        // Prepare mock response data
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(2L);  // Example user ID
        users.add(user1);

        // Set up the mock response
        given(lobbyService.getUsersInLobby(lobbyId)).willReturn(users);

        // Perform the test
        mockMvc.perform(get("/lobby/user/{lobbyId}", lobbyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user1.getId().intValue())));  // Check the first user's ID
    }


    @Test
    public void whenExitLobby_withValidUserAndLobby_thenNoContent() throws Exception {
        Long userId = 1L;
        Long lobbyId = 1L;
        User user = new User();
        user.setId(userId);
        Lobby lobby = new Lobby();
        lobby.setId(lobbyId);

        given(userService.getUserById(userId)).willReturn(user);
        given(lobbyService.removeUser(lobbyId, user)).willReturn(lobby);

        mockMvc.perform(post("/lobby/exit/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId)))
                .andExpect(status().isNoContent());
    }
    */
}
