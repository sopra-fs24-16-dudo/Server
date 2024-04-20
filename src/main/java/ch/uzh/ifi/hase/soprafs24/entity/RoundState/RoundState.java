package ch.uzh.ifi.hase.soprafs24.entity.RoundState;
import ch.uzh.ifi.hase.soprafs24.entity.Bid;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.Suit;

import java.util.List;
import java.util.Map;

public interface RoundState {


    Bid placeBid(Bid bid, List <Bid> validBids);
    Player dudo(Bid currentBid, Map <Suit, Long> suitCounter, List<Player> players,Player currentPlayer, Player lastPlayer);
    Long count (Suit suit, Player player);
    List <Bid> getValidBids(Bid CurrentBid, Player bidder);
    Map<Suit, Long> getSuitCounter(List <Player> players);

}
