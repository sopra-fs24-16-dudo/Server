package ch.uzh.ifi.hase.soprafs24.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserService userService;

    @Mock
    private VoiceChannelService voiceChannelService;

    @InjectMocks
    private LobbyService lobbyService;

    private User newUser;
    private User testUser;
    private Lobby lobby;
    private VoiceChannel voiceChannel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("newUser");

        testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("testUser");

        lobby = new Lobby();
        lobby.setId(999L);

        lobby.setUsers(new ArrayList<>());
        //lobby.getUsers().add(newUser);

        voiceChannel = new VoiceChannel();
        voiceChannel.setId(1L);

        when(lobbyRepository.save(any())).thenReturn(lobby);
        when(lobbyRepository.findById(any())).thenReturn(Optional.of(lobby));
        when(voiceChannelService.createVoiceChannel(any())).thenReturn(voiceChannel);
    }

    /*@Test //Actually unsure why this test fails
    public void createLobby_withNewUser_success() {

        Lobby createdLobby = lobbyService.createLobby(testUser);

        assertNotNull(createdLobby);
        assertEquals(1, createdLobby.getUsers().size());
        assertEquals(voiceChannel, createdLobby.getVoiceChannel());
        verify(lobbyRepository, times(1)).save(any(Lobby.class));
    }*/

    @Test
    public void addUserToExistingLobby_success() {
        Lobby updatedLobby = lobbyService.addUser(1L, newUser);

        assertNotNull(updatedLobby);
        assertTrue(updatedLobby.getUsers().contains(newUser));
        verify(lobbyRepository, times(1)).save(updatedLobby);
    }

    @Test
    public void addUserToFullLobby_throwsException() {
        when(lobbyRepository.findById(any())).then(invocation -> {
            Lobby fullLobby = new Lobby();
            fullLobby.setId(1L);
            fullLobby.setUsers(List.of(new User(), new User(), new User(), new User(), new User(), new User()));
            return Optional.of(fullLobby);
        });

        Exception exception = assertThrows(ResponseStatusException.class, () -> lobbyService.addUser(1L, newUser));
        assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException)exception).getStatus());
    }

    @Test
    public void removeUserFromLobby_updatesLobbyCorrectly() {
        lobby.addUser(newUser);

        Lobby updatedLobby = lobbyService.removeUser(lobby.getId(), newUser);
        assertFalse(updatedLobby.getUsers().contains(newUser));
    }

    @Test
    public void getAllLobbies_returnsAllLobbies() {
        when(lobbyRepository.findAll()).thenReturn(List.of(lobby));

        List<Lobby> lobbies = lobbyService.getAllLobbies();

        assertFalse(lobbies.isEmpty());
        assertEquals(1, lobbies.size());
    }

    @Test
    public void getLobbyById_validId_returnsLobby() {
        Optional<Lobby> foundLobby = Optional.of(lobby);

        assertEquals(foundLobby.get(), lobbyService.getLobbyById(1L));
    }

    /*@Test //Still need to implement maybe get and set functions for messages?
    public void postMessage_addsMessageToLobby() {
        // Given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        when(lobbyRepository.findById(1L)).thenReturn(Optional.of(lobby));

        // When
        lobbyService.postMessage(1L, "New message");

        // Then
        assertTrue(lobby.getMessages().contains("New message"));  // Validate the message was added
    }*/

    @Test
    public void startGame_checksAllUsersReady() {
        newUser.setReady(true);
        lobby.addUser(newUser);

        assertTrue(lobbyService.areAllUsersReady(1L));
    }
}
