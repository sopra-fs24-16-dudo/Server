package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Mock
    private LobbyService lobbyService;

    @InjectMocks
    private GameService gameService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
    }

    @Test
    public void createPlayer_validUser_success() {
        // when
        Player createdPlayer = gameService.createPlayer(testUser);

        // then
        assertNotNull(createdPlayer);
        assertEquals(testUser.getId(), createdPlayer.getId());
        assertEquals(testUser.getUsername(), createdPlayer.getUsername());
    }

    @Test
    public void createPlayer_nullUser_throwsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            gameService.createPlayer(null);
        });

    }

    // Add more tests if needed for other functionalities in GameService
}
