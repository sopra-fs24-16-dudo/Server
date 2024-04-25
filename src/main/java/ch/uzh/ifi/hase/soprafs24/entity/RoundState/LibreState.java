package ch.uzh.ifi.hase.soprafs24.entity.RoundState;

import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibreState implements RoundState {

    @Override
    public Bid placeBid(Bid bid, List <Bid> validBids) {
        //check if the values of the bid match any of the valid bids
        for (Bid validBid : validBids) {
            if (bid.getSuit() == validBid.getSuit() && bid.getAmount() == validBid.getAmount()){
                return bid;
            }
        }
        throw new IllegalArgumentException("Invalid bid");
    }

    @Override
    public Long dudo(Bid currentBid, Map <Suit, Long> suitCounter, Player currentPlayer, Player lastPlayer) {
        if (currentPlayer.getId() == lastPlayer.getId()){
            throw new IllegalArgumentException("Cannot dudo on the first bid");
        }
        Long bidAmount = currentBid.getAmount();
        Long totalAmount = suitCounter.get(currentBid.getSuit());
        if (totalAmount <= bidAmount){
            return currentPlayer.getId();
        }
        return lastPlayer.getId();
    }

    @Override
    public Long count(Suit suit, Player player) {
        if (suit.equals(Suit.ACE)){
            return player.countSuit(Suit.ACE);
        }
        return player.countSuit(suit) + player.countSuit(Suit.ACE);
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
                if (newBid.getAmount() > currentBid.getAmount() ||
                        (newBid.getAmount() == currentBid.getAmount() && newBid.getSuit().compareTo(currentBid.getSuit()) > 0) ||
                        (currentBid.getSuit() == Suit.ACE && newBid.getAmount() >= currentBid.getAmount() * 2) ||
                        (newBid.getSuit() == Suit.ACE && newBid.getAmount() == currentBid.getAmount() / 2 + 1)) {
                    validBids.add(newBid);
                }
            }
        }
        return validBids;
    }

    @Override
    public Map<Suit, Long> getSuitCounter(List<Player> players) {
        Map<Suit, Long> map = new HashMap<>();
        Long aceCount = 0L;

        // Count the number of aces first
        for (Player player : players) {
            aceCount += count(Suit.ACE, player);
        }

        // Initialize the map with the count of aces for each suit
        for (Suit suit : Suit.values()) {
            map.put(suit, aceCount);
        }

        // For each suit, add the number of dices with that suit in all players' hand to the map
        for (Player player : players) {
            for (Suit suit : Suit.values()) {
                if (suit != Suit.ACE) {
                    map.put(suit, map.get(suit) + count(suit, player));
                }
            }
        }

        return map;
    }

    @Override
    public Bid getNextBid(Bid currentBid, Player bidder, Long playerSize) {
        if (currentBid.getSuit() == null){
            return new Bid(Suit.NINE, 1L);
        }
        if (currentBid.getAmount() >= playerSize * 5)
            return getValidBids(currentBid, bidder, playerSize).get(0);
        return new Bid(currentBid.getSuit(), currentBid.getAmount() + 1);
    }
}
