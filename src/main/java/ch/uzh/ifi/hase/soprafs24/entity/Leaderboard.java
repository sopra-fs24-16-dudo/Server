package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Leaderboard {

    private Map<Player, Long> userPoints;

    public Leaderboard() {
        this.userPoints = new HashMap<>();
    }


    public void addUser(Player player) {
        boolean playerExists = this.userPoints.keySet().stream()
                .anyMatch(existingPlayer -> existingPlayer.getId() == player.getId());
        if (!playerExists) {
            this.userPoints.put(player, 0L);
        }
    }

    public void addPoints(Player winner, Long points) {
        Player matchingPlayer = this.userPoints.keySet().stream()
                .filter(existingPlayer -> existingPlayer.getId() == winner.getId())
                .findFirst()
                .orElse(null);
        if (matchingPlayer != null) {
            Long currentPoints = this.userPoints.get(matchingPlayer);
            this.userPoints.put(matchingPlayer, currentPoints + points);
        }
    }

    public void removePlayer(Player player) {
        this.userPoints.remove(player);
    }

    public void resetPoints() {
        this.userPoints.clear();
    }

    public Map<Player, Long> getUserPoints() {
        return userPoints;
    }


    @Override
    public String toString() {
        return this.userPoints.entrySet().stream()
                .map(entry -> entry.getKey().getUsername() + " " + entry.getValue())
                .collect(Collectors.joining(","));
    }
}
