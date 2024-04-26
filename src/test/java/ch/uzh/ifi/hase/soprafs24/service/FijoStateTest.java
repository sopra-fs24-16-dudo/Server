package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;

import ch.uzh.ifi.hase.soprafs24.entity.RoundState.FijoState;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FijoStateTest {

    @Test
    void testPlaceBid_Valid() {
        FijoState state = new FijoState();
        List<Bid> validBids = List.of(new Bid(Suit.ACE, 2L));
        Bid bid = new Bid(Suit.ACE, 2L);

        Bid result = state.placeBid(bid, validBids);
        assertEquals(bid, result, "Valid bid should be accepted");
    }

    @Test
    void testPlaceBid_Invalid() {
        FijoState state = new FijoState();
        List<Bid> validBids = List.of(new Bid(Suit.ACE, 3L));
        Bid bid = new Bid(Suit.ACE, 2L);

        assertThrows(IllegalArgumentException.class, () -> state.placeBid(bid, validBids), "Invalid bid should throw an exception");
    }
    @Test
    void testDudo_PlayerWins() {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);

        FijoState state = new FijoState();
        Bid currentBid = new Bid(Suit.ACE, 2L);
        Map<Suit, Long> suitCounter = Map.of(Suit.ACE, 3L);
        Player currentPlayer = new Player(u1);
        Player lastPlayer = new Player(u2);

        Long result = state.dudo(currentBid, suitCounter, currentPlayer, lastPlayer);

        assertEquals(currentPlayer.getId(), result, "Current player should win when total amount is greater than or equal to bid amount");
    }

    @Test
    void testDudo_LastPlayerWins() {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);

        FijoState state = new FijoState();
        Bid currentBid = new Bid(Suit.ACE, 2L);
        Map<Suit, Long> suitCounter = Map.of(Suit.ACE, 1L);
        Player currentPlayer = new Player(u1);
        Player lastPlayer = new Player(u2);

        Long result = state.dudo(currentBid, suitCounter, currentPlayer, lastPlayer);

        assertEquals(lastPlayer.getId(), result, "Last player should win when total amount is less than bid amount");
    }

    @Test
    void testGetValidBids_NoCurrentBid() {
        User u1 = new User();
        u1.setId(1L);
        FijoState state = new FijoState();
        Bid currentBid = new Bid(null, null);
        Player bidder = new Player(u1);
        Long playerSize = 3L;

        List<Bid> validBids = state.getValidBids(currentBid, bidder, playerSize);

        assertEquals(90, validBids.size(), "Valid bids should be generated for each suit and value from 1 to 5 times the player size");
    }

    @Test
    void testGetNextBid_NoCurrentBid() {
        User u1 = new User();
        u1.setId(1L);
        FijoState state = new FijoState();
        Bid currentBid = new Bid(null, null);
        Player bidder = new Player(u1);
        Long playerSize = 3L;

        Bid nextBid = state.getNextBid(currentBid, bidder, playerSize);

        assertEquals(new Bid(Suit.NINE, 1L), nextBid, "Next bid should be the minimum bid when no current bid exists");
    }
}
