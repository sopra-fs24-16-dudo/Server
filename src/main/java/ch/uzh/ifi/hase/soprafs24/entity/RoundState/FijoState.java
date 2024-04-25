package ch.uzh.ifi.hase.soprafs24.entity.RoundState;

import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FijoState implements RoundState {

    @Override
    public Bid placeBid(Bid bid, List<Bid> validBids) {
        if (!validBids.contains(bid)){
            throw new IllegalArgumentException("Invalid bid");
        }
        return bid;
    }

    @Override
    public Long dudo(Bid currentBid, Map <Suit, Long> suitCounter, Player currentPlayer, Player lastPlayer) {
        Long bidAmount = currentBid.getAmount();
        Long totalAmount = suitCounter.get(currentBid.getSuit());
        if (totalAmount <= bidAmount){
            return currentPlayer.getId();
        }
        return lastPlayer.getId();
    }

    @Override
    public Long count(Suit suit, Player player) {
        return player.countSuit(suit);
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
        if (bidder.getChips() == 1){
            for (Suit suit : Suit.values()) {
                for (Long value = 1L; value <= maxAmount; value++) {
                    Bid newBid = new Bid(suit, value);
                    if (newBid.getAmount() > currentBid.getAmount() ||
                            (newBid.getAmount() == currentBid.getAmount() &&
                                    newBid.getSuit().compareTo(currentBid.getSuit()) > 0)
                    )
                        validBids.add(newBid);
                }
            }
        }else {
            for (Suit suit : Suit.values()) {
                for (Long value = 1L; value <= maxAmount; value++) {
                    Bid newBid = new Bid(suit, value);
                    if (newBid.getAmount() > currentBid.getAmount() && newBid.getSuit().equals(currentBid.getSuit())) {
                        validBids.add(newBid);
                    }
                }
            }
        }
        return validBids;
    }

    @Override
    public Map<Suit, Long> getSuitCounter(List<Player> players) {
        Map<Suit, Long> map = new HashMap<>();
        //initialize the map with 0 for each suit
        //for each suit, count the number of dices with that suit in all players' hand and return the map
        for (Player player : players) {
            for (Suit suit : Suit.values()) {
                if (map.containsKey(suit)){
                    map.put(suit, map.get(suit) + count(suit, player));
                } else {
                    map.put(suit, count(suit, player));
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
