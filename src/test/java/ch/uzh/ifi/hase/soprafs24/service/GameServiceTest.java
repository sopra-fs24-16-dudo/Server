package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
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

    private User invalidUser;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize the test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");

        invalidUser = null;
    }

    @Test
    public void createPlayer_validUser_success() {
        // Execute the method to test
        Player createdPlayer = gameService.createPlayer(testUser);

        // Verify the result
        assertNotNull(createdPlayer, "The player should not be null");
        assertSame(testUser.getId(), createdPlayer.getId(), "The player should have the correct user Id associated");
        assertSame(testUser.getUsername(), createdPlayer.getUsername(), "The player should have the correct username associated");
    }
    @Test
    public void createPlayer_nullUser_throwsException() {
        // Attempt to create a player with a null user and expect an exception
        assertThrows(NullPointerException.class, () -> gameService.createPlayer(invalidUser),
                "Expected createPlayer to throw IllegalArgumentException for null user input");
    }
}