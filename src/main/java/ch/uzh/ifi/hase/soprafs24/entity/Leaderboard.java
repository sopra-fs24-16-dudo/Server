package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.Map;
import java.util.HashMap;

public class Leaderboard {

    private Map<User, Long> userPoints;

    public Leaderboard() {
        this.userPoints = new HashMap<>();
    }

    public Map<User, Long> getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(Map<User, Long> userPoints) {
        this.userPoints = userPoints;
    }

    public void addUser(User user) {
        Long currentPoints = this.userPoints.get(user);
        if (currentPoints == null) {
            // Der Benutzer ist noch nicht in der Map, also fügen wir ihn mit einem Punkt hinzu.
            this.userPoints.put(user, 0L);
        }
    }

    public void updatePoints(User user) {
        Long currentPoints = this.userPoints.get(user);
        this.userPoints.put(user, currentPoints + 1);
    }
}
