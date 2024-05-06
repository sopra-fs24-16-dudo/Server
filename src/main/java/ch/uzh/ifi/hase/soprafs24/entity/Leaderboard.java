package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Leaderboard {

    private Map<Player, Long> userPoints;

    public Leaderboard() {
        this.userPoints = new HashMap<>();
    }

    public Map<Player, Long> getUserPoints() {
        return userPoints;
    }

    public void addUser(Player player) {
        Long currentPoints = this.userPoints.get(player);
        if (currentPoints == null) {
            this.userPoints.put(player, 0L);
        }
    }

   public void addPoints(Player player, Long points) {
        Long currentPoints = this.userPoints.get(player);
        if (currentPoints == null) {
            this.userPoints.put(player, points);
        } else {
            this.userPoints.put(player, currentPoints + points);
        }
    }

    public void removePlayer(Player player) {
        this.userPoints.remove(player);
    }

    public void resetPoints() {
        this.userPoints.clear();
    }

    @Override
    public String toString() {
        return this.userPoints.entrySet().stream()
                .map(entry -> entry.getKey().getUsername() + " " + entry.getValue())
                .collect(Collectors.joining(","));
    }
}
