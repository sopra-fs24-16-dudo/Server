package ch.uzh.ifi.hase.soprafs24.entity.RoundState;

import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Dice;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Suit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibreState implements RoundState {

    @Override
    public Bid placeBid(Bid bid, List <Bid> validBids) {
        for (Bid validBid : validBids) {
            if (bid.getSuit() == validBid.getSuit() && bid.getAmount() == validBid.getAmount()){
                return bid;
            }
        }
        throw new IllegalArgumentException("Invalid bid");
    }

    @Override
    public Long dudo(Bid currentBid, Map <Suit, Long> suitCounter, Player currentPlayer, Player lastPlayer) {
        Long bidAmount = currentBid.getAmount();
        Long totalAmount = suitCounter.get(currentBid.getSuit());
        if (totalAmount >= bidAmount){
            return currentPlayer.getId();
        }
        return lastPlayer.getId();
    }

    @Override
    public List<Bid> getValidBids(Bid currentBid, Player bidder, Long playerSize) {
        List<Bid> validBids = new ArrayList<>();
        Long maxAmount = playerSize * 5;
        if (currentBid.getSuit() == null){
            for (Suit suit : Suit.values()) {
                for (Long value = 1L; value <= maxAmount; value++) {
                    Bid newBid = new Bid(suit, value);
                    validBids.add(newBid);
                }
            }
            return validBids;
        }
        for (Suit suit : Suit.values()) {
            for (Long value = 1L; value <= maxAmount; value++) {
                Bid newBid = new Bid(suit, value);
                if (currentBid.getSuit() == Suit.ACE) {
                    if (newBid.getAmount() >= currentBid.getAmount() *2 ||
                            (newBid.getSuit() == Suit.ACE && newBid.getAmount() > currentBid.getAmount())
                        )
                        validBids.add(newBid);
                }
                else if (newBid.getAmount() > currentBid.getAmount() ||
                        (newBid.getAmount() == currentBid.getAmount() && newBid.getSuit().compareTo(currentBid.getSuit()) > 0) ||
                        (currentBid.getSuit() == Suit.ACE && newBid.getAmount() >= currentBid.getAmount() * 2) ||
                        (newBid.getSuit() == Suit.ACE && newBid.getAmount() >= currentBid.getAmount() / 2 + 1)) {
                    validBids.add(newBid);
                }
            }
        }
        return validBids;
    }

    @Override
    public Map<Suit, Long> getSuitCounter(List<Player> players) {
        Map<Suit, Long> suitCounter = new HashMap<>();
        for (Suit suit : Suit.values()) {
            suitCounter.put(suit, 0L);
        }
        for (Player player : players) {
            if (player.getChips() > 0) {
                for (Dice dice : player.getHand().getDices()) {
                    if (dice.getSuit() != Suit.ACE)
                        suitCounter.put(dice.getSuit(), suitCounter.get(dice.getSuit()) + 1);
                    if (dice.getSuit() == Suit.ACE) {
                        for (Suit suit : Suit.values()) {
                            suitCounter.put(suit, suitCounter.get(suit) + 1);
                        }
                    }
                }
            }
        }
        return suitCounter;
    }

    @Override
    public Bid getNextBid(Bid currentBid, Player bidder, Long playerSize) {
        if (currentBid.getSuit() == null){
            return new Bid(Suit.NINE, 1L);
        }
        if (currentBid.getAmount() >= playerSize * 5 && getValidBids(currentBid, bidder, playerSize).size() > 0)
            return getValidBids(currentBid, bidder, playerSize).get(0);
        else if (getValidBids(currentBid, bidder, playerSize).size() == 0)
            return null;
        return new Bid(currentBid.getSuit(), currentBid.getAmount() + 1);
    }
}
