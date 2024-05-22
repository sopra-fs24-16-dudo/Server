package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.FijoState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FijoStateTest {

    private FijoState state;
    private User user1;
    private User user2;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        state = new FijoState();
        user1 = new User();
        user1.setId(1L);
        user2 = new User();
        user2.setId(2L);
        player1 = new Player(user1);
        player2 = new Player(user2);
    }

    @Test
    void testPlaceBid_Valid() {
        List<Bid> validBids = List.of(new Bid(Suit.ACE, 2L));
        Bid bid = new Bid(Suit.ACE, 2L);

        Bid result = state.placeBid(bid, validBids);
        assertEquals(bid, result, "Valid bid should be accepted");
    }

    @Test
    void testPlaceBid_Invalid() {
        List<Bid> validBids = List.of(new Bid(Suit.ACE, 3L));
        Bid bid = new Bid(Suit.ACE, 2L);

        assertThrows(IllegalArgumentException.class, () -> state.placeBid(bid, validBids), "Invalid bid should throw an exception");
    }

    @Test
    void testDudo_PlayerWins() {
        Bid currentBid = new Bid(Suit.ACE, 2L);
        Map<Suit, Long> suitCounter = Map.of(Suit.ACE, 3L);

        Long result = state.dudo(currentBid, suitCounter, player1, player2);

        assertEquals(player1.getId(), result, "Current player should win when total amount is greater than or equal to bid amount");
    }

    @Test
    void testDudo_LastPlayerWins() {
        Bid currentBid = new Bid(Suit.ACE, 2L);
        Map<Suit, Long> suitCounter = Map.of(Suit.ACE, 1L);

        Long result = state.dudo(currentBid, suitCounter, player1, player2);

        assertEquals(player2.getId(), result, "Last player should win when total amount is less than bid amount");
    }

    @Test
    void testGetValidBids_NoCurrentBid() {
        Bid currentBid = new Bid(null, null);
        Long playerSize = 3L;

        List<Bid> validBids = state.getValidBids(currentBid, player1, playerSize);

        assertEquals(90, validBids.size(), "Valid bids should be generated for each suit and value from 1 to 5 times the player size");
    }

    @Test
    void testGetValidBids_SingleChip() {
        player1.setChips(1);
        Bid currentBid = new Bid(Suit.ACE, 1L);
        Long playerSize = 3L;

        List<Bid> validBids = state.getValidBids(currentBid, player1, playerSize);

        assertFalse(validBids.isEmpty(), "Valid bids should be generated for single chip player");
    }

    @Test
    void testGetValidBids_MultipleChips() {
        player1.setChips(2);
        Bid currentBid = new Bid(Suit.ACE, 1L);
        Long playerSize = 3L;

        List<Bid> validBids = state.getValidBids(currentBid, player1, playerSize);

        assertFalse(validBids.isEmpty(), "Valid bids should be generated for multiple chips player");
    }

    @Test
    void testGetNextBid_NoCurrentBid() {
        Bid currentBid = new Bid(null, null);
        Long playerSize = 3L;

        Bid nextBid = state.getNextBid(currentBid, player1, playerSize);

        assertEquals(new Bid(Suit.NINE, 1L), nextBid, "Next bid should be the minimum bid when no current bid exists");
    }

    @Test
    void testGetNextBid_MaxAmount() {
        Bid currentBid = new Bid(Suit.ACE, 1L);
        Long playerSize = 3L;

        Bid nextBid = state.getNextBid(currentBid, player1, playerSize);

        assertNotNull(nextBid, "Next bid should not be null if valid bids are available");
    }

    @Test
    void testGetNextBid_NoValidBids() {
        Bid currentBid = new Bid(Suit.ACE, 20L);
        Long playerSize = 3L;

        Bid nextBid = state.getNextBid(currentBid, player1, playerSize);

        assertNull(nextBid, "Next bid should be null if no valid bids are available");
    }

    @Test
    void testGetNextBid_IncreaseAmount() {
        Bid currentBid = new Bid(Suit.ACE, 2L);
        Long playerSize = 3L;

        Bid nextBid = state.getNextBid(currentBid, player1, playerSize);

        assertEquals(new Bid(Suit.ACE, 3L), nextBid, "Next bid should increase the amount by 1");
    }
}
