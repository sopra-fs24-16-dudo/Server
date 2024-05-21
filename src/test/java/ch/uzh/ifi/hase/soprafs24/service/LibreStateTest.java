package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Dice;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.LibreState;
import ch.uzh.ifi.hase.soprafs24.entity.Suit;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LibreStateTest {
    @Test
    void testPlaceBid_Valid() {
        LibreState state = new LibreState();
        List<Bid> validBids = List.of(new Bid(Suit.ACE, 2L));
        Bid bid = new Bid(Suit.ACE, 2L);

        Bid result = state.placeBid(bid, validBids);
        assertEquals(bid, result, "Valid bid should be accepted");
    }

    @Test
    void testPlaceBid_Invalid() {
        LibreState state = new LibreState();
        List<Bid> validBids = List.of(new Bid(Suit.ACE, 3L));
        Bid bid = new Bid(Suit.ACE, 2L);

        assertThrows(IllegalArgumentException.class, () -> state.placeBid(bid, validBids), "Invalid bid should throw an exception");
    }

    @Test
    void testDudo_CorrectPlayerWins() {
        LibreState state = new LibreState();
        Bid currentBid = new Bid(Suit.ACE, 5L);
        Map<Suit, Long> suitCounter = new HashMap<>();
        suitCounter.put(Suit.ACE, 4L);

        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("last");

        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("last");
        Player currentPlayer = new Player(u1);
        Player lastPlayer = new Player(u2);

        Long winnerId = state.dudo(currentBid, suitCounter, currentPlayer, lastPlayer);
        assertEquals(lastPlayer.getId(), winnerId, "Last player should win when the bid is too high");
    }

    /*@Test
    void testGetValidBids_NullCurrentBid() {
        LibreState state = new LibreState();
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("bidder");
        Player bidder = new Player(u1);
        List<Bid> result = state.getValidBids(new Bid(Suit.ACE, 1L), bidder, 2L);

        assertFalse(result.isEmpty(), "Should generate initial valid bids");
        assertTrue(result.contains(new Bid(Suit.ACE, 1L)), "Should include ace bids");
    }*/

    /*@Test
    void testGetSuitCounter_CorrectCounts() {
        LibreState state = new LibreState();
        List<Player> players = List.of(
                new Player(new User(1L, "Alice"), List.of(new Dice(Suit.ACE), new Dice(Suit.getSuit("HEARTS")))),
                new Player(new User(2L, "Bob"), List.of(new Dice(Suit.ACE), new Dice(Suit.SPADES)))
        );

        Map<Suit, Long> counts = state.getSuitCounter(players);
        assertEquals(2, counts.get(Suit.ACE).longValue(), "Aces should be counted correctly for all suits");
        assertEquals(1, counts.get(Suit.HEARTS).longValue(), "Hearts should be counted correctly");
        assertEquals(1, counts.get(Suit.SPADES).longValue(), "Spades should be counted correctly");
    }

    @Test
    void testGetNextBid_IncrementBid() {
        LibreState state = new LibreState();
        Bid currentBid = new Bid(Suit.HEARTS, 2L);
        Player bidder = new Player(new User(1L, "bidder"));

        Bid nextBid = state.getNextBid(currentBid, bidder, 3L);
        assertEquals(3L, nextBid.getAmount(), "The next bid amount should be incremented");
    }*/
}


