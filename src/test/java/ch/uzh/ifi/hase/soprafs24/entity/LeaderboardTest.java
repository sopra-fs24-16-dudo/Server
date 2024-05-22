package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardTest {

    private Leaderboard leaderboard;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        leaderboard = new Leaderboard();

        // Create users
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        // Create players
        player1 = new Player(user1);
        player2 = new Player(user2);
    }

    @Test
    void testAddUser_NewUser() {
        leaderboard.addUser(player1);

        Map<Player, Long> userPoints = leaderboard.getUserPoints();
        assertEquals(1, userPoints.size());
        assertTrue(userPoints.containsKey(player1));
        assertEquals(0L, userPoints.get(player1));
    }

    @Test
    void testAddUser_ExistingUser() {
        leaderboard.addUser(player1);
        leaderboard.addUser(player1);

        Map<Player, Long> userPoints = leaderboard.getUserPoints();
        assertEquals(1, userPoints.size());
    }

    @Test
    void testAddPoints_NewUser() {
        leaderboard.addPoints(player1, 10L);

        Map<Player, Long> userPoints = leaderboard.getUserPoints();
        assertFalse(userPoints.containsKey(player1));
    }

    @Test
    void testAddPoints_ExistingUser() {
        leaderboard.addUser(player1);
        leaderboard.addPoints(player1, 10L);

        Map<Player, Long> userPoints = leaderboard.getUserPoints();
        assertEquals(10L, userPoints.get(player1));
    }

    @Test
    void testAddPoints_MultipleUsers() {
        leaderboard.addUser(player1);
        leaderboard.addUser(player2);
        leaderboard.addPoints(player1, 10L);
        leaderboard.addPoints(player2, 5L);

        Map<Player, Long> userPoints = leaderboard.getUserPoints();
        assertEquals(10L, userPoints.get(player1));
        assertEquals(5L, userPoints.get(player2));
    }

    @Test
    void testToString() {
        leaderboard.addUser(player1);
        leaderboard.addUser(player2);
        leaderboard.addPoints(player1, 10L);
        leaderboard.addPoints(player2, 5L);

        String expected = "user2 5,user1 10";

        assertEquals(expected, leaderboard.toString());
    }

    // Adding getter for userPoints to be able to access it in tests
    @Test
    void testGetUserPoints() {
        leaderboard.addUser(player1);
        leaderboard.addUser(player2);

        Map<Player, Long> userPoints = leaderboard.getUserPoints();
        assertEquals(2, userPoints.size());
        assertEquals(0L, userPoints.get(player1));
        assertEquals(0L, userPoints.get(player2));
    }
}
