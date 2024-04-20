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
        if (!validBids.contains(bid)){
            throw new IllegalArgumentException("Invalid bid");
        }
        return bid;
    }

    @Override
    public Player dudo(Bid currentBid, Map <Suit, Long> suitCounter, List<Player> players, Player currentPlayer,
                       Player lastPlayer) {
        Long bidAmount = currentBid.getAmount();
        Long totalAmount = 0L;
        if (totalAmount <= bidAmount){
            return currentPlayer;
        }
        return lastPlayer;
    }

    @Override
    public Long count(Suit suit, Player player) {
        if (suit.equals(Suit.ACE)){
            return player.countSuit(Suit.ACE);
        }
        return player.countSuit(suit) + player.countSuit(Suit.ACE);
    }

    @Override
    public List<Bid> getValidBids(Bid currentBid, Player bidder){
        List<Bid> validBids = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Long value = 1L; value <= 6; value++) {
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
}
