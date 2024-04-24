package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.Map;
import java.util.HashMap;

public class Leaderboard {

    private Map<Player, Long> userPoints;

    public Leaderboard() {
        this.userPoints = new HashMap<>();
    }

    //public Map<User, Long> getUserPoints() {
       // return userPoints;
  //  }

    public void setUserPoints(Map<Player, Long> userPoints) {
        this.userPoints = userPoints;
    }

    public void addUser(Player player) {
        Long currentPoints = this.userPoints.get(player);
        if (currentPoints == null) {
            // Der Benutzer ist noch nicht in der Map, also fügen wir ihn mit einem Punkt hinzu.
            this.userPoints.put(player, 0L);
        }
    }

   // public void updatePoints(User user) {
   //     Long currentPoints = this.userPoints.get(user);
   //     this.userPoints.put(user, currentPoints + 1);
   // }
}
