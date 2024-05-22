package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SuitTest {

    @Test
    void testSuitValues() {
        assertEquals(1, Suit.NINE.ordinal() + 1);
        assertEquals(2, Suit.TEN.ordinal() + 1);
        assertEquals(3, Suit.JACK.ordinal() + 1);
        assertEquals(4, Suit.QUEEN.ordinal() + 1);
        assertEquals(5, Suit.KING.ordinal() + 1);
        assertEquals(6, Suit.ACE.ordinal() + 1);
    }

    @Test
    void testGetSuit() {
        assertEquals(Suit.NINE, Suit.getSuit("NINE"));
        assertEquals(Suit.TEN, Suit.getSuit("TEN"));
        assertEquals(Suit.JACK, Suit.getSuit("JACK"));
        assertEquals(Suit.QUEEN, Suit.getSuit("QUEEN"));
        assertEquals(Suit.KING, Suit.getSuit("KING"));
        assertEquals(Suit.ACE, Suit.getSuit("ACE"));
    }

    @Test
    void testGetSuit_Invalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Suit.getSuit("INVALID");
        });
        assertEquals("Invalid suit", exception.getMessage());
    }

    @Test
    void testOrdinalValues() {
        assertEquals(0, Suit.NINE.ordinal());
        assertEquals(1, Suit.TEN.ordinal());
        assertEquals(2, Suit.JACK.ordinal());
        assertEquals(3, Suit.QUEEN.ordinal());
        assertEquals(4, Suit.KING.ordinal());
        assertEquals(5, Suit.ACE.ordinal());
    }
}
