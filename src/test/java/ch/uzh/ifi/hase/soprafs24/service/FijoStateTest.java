package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.RoundState.FijoState;
import ch.uzh.ifi.hase.soprafs24.entity.Suit;
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

}
