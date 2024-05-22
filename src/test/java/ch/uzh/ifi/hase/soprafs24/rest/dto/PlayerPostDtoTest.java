package ch.uzh.ifi.hase.soprafs24.rest.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPostDTOTest {

    private PlayerPostDTO playerPostDTO;

    @BeforeEach
    void setUp() {
        playerPostDTO = new PlayerPostDTO();
    }

    @Test
    void testGetUsername() {
        playerPostDTO.setUsername("testUser");
        assertEquals("testUser", playerPostDTO.getUsername());
    }

    @Test
    void testSetUsername() {
        playerPostDTO.setUsername("testUser");
        assertEquals("testUser", playerPostDTO.getUsername());
    }

    @Test
    void testGetGamesPlayed() {
        playerPostDTO.setGamesPlayed(5);
        assertEquals(5, playerPostDTO.getGamesPlayed());
    }

    @Test
    void testSetGamesPlayed() {
        playerPostDTO.setGamesPlayed(5);
        assertEquals(5, playerPostDTO.getGamesPlayed());
    }

    @Test
    void testGetGamesWon() {
        playerPostDTO.setGamesWon(3);
        assertEquals(3, playerPostDTO.getGamesWon());
    }

    @Test
    void testSetGamesWon() {
        playerPostDTO.setGamesWon(3);
        assertEquals(3, playerPostDTO.getGamesWon());
    }

    @Test
    void testGetWinRatio() {
        playerPostDTO.setWinRatio(0.6);
        assertEquals(0.6, playerPostDTO.getWinRatio());
    }

    @Test
    void testSetWinRatio() {
        playerPostDTO.setWinRatio(0.6);
        assertEquals(0.6, playerPostDTO.getWinRatio());
    }

    @Test
    void testIsReady() {
        playerPostDTO.setReady(true);
        assertTrue(playerPostDTO.isReady());
    }

    @Test
    void testSetReady() {
        playerPostDTO.setReady(true);
        assertTrue(playerPostDTO.isReady());
    }
}
