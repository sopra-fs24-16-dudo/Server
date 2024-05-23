package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Clear existing data before each test
        lobbyService.clearAllLobbies();
        userService.clearAllUsers();
    }

    private User createUser(String username, String name) {
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.ONLINE);
        return userService.createUser(user);
    }

    /*@Test
    public void getAllLobbies_thenReturnJsonArray() throws Exception {
        // given
        User user = createUser("testUser", "Test User");
        Player player = new Player(user);

        Lobby lobby = lobbyService.createLobby(player);

        // when
        mockMvc.perform(get("/lobbies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(lobby.getId())));
    }*/

    @Test
    public void createLobby_validInput_lobbyCreated() throws Exception {
        // given
        User user = createUser("testUser", "Test User");

        // when/then
        mockMvc.perform(post("/lobbies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.adminId", is(user.getId().intValue())));
    }

    @Test
    public void addPlayerToLobby_validInput_noContent() throws Exception {
        // given
        User user = createUser("testUser", "Test User");
        Player player = new Player(user);

        Lobby lobby = lobbyService.createLobby(player);

        // when/then
        mockMvc.perform(put("/lobbies/players/" + lobby.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user.getId())))
                .andExpect(status().isNoContent());
    }

    /*@Test
    public void getPlayersFromLobby_thenReturnJsonArray() throws Exception {
        // given
        User user = createUser("testUser", "Test User");
        Player player = new Player(user);

        Lobby lobby = lobbyService.createLobby(player);
        lobbyService.addPlayer(lobby.getId(), player);

        // when/then
        mockMvc.perform(get("/lobby/players/" + lobby.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user.username", is(user.getUsername())));
    }*/

    /*
    @Test
    public void exitLobby_validInput_noContent() throws Exception {
        // given
        User user = createUser("testUser", "Test User");
        Player player = new Player(user);

        Lobby lobby = lobbyService.createLobby(player);
        lobbyService.addPlayer(lobby.getId(), player);

        // when/then
        mockMvc.perform(post("/lobby/exit/" + lobby.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player.getId())))
                .andExpect(status().isOk());
    }

     */

    @Test
    public void kickPlayerFromLobby_validInput_noContent() throws Exception {
        // given
        User user = createUser("testUser", "Test User");
        Player player = new Player(user);

        Lobby lobby = lobbyService.createLobby(player);
        lobbyService.addPlayer(lobby.getId(), player);

        // when/then
        mockMvc.perform(post("/lobbies/kick/" + lobby.getId() + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserReadyStatus_validInput_noContent() throws Exception {
        // given
        User user = createUser("testUser", "Test User");
        Player player = new Player(user);

        Lobby lobby = lobbyService.createLobby(player);
        lobbyService.addPlayer(lobby.getId(), player);

        // when/then
        mockMvc.perform(put("/lobbies/player/" + lobby.getId() + "/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user.getId())))
                .andExpect(status().isOk());
    }
}
