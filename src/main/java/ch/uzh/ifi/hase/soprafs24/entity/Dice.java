package ch.uzh.ifi.seal.soprafs24.entity;

import java.util.*;

public class Dice {
    private int numberOfDice;
    private List<String> diceValues;
    private Map<Integer, String> diceMapping;

    public Dice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
        this.diceValues = new ArrayList<>();
        this.diceMapping = createDiceMapping();
        rollDice();
    }

    // Roll the dice
    public void rollDice() {
        diceValues.clear();
        Random random = new Random();
        for (int i = 0; i < numberOfDice; i++) {
            int diceValue = random.nextInt(6) + 1; // 6-sided dice
            diceValues.add(diceMapping.get(diceValue));
        }
    }

    // Roll a single dice
    public String rollSingleDice() {
        Random random = new Random();
        int diceValue = random.nextInt(6) + 1; // 6-sided dice
        return diceMapping.get(diceValue);
    }

    // Get the total value of all dice
    public int getTotalDiceValue() {
        int total = 0;
        for (String value : diceValues) {
            total += Integer.parseInt(value);
        }
        return total;
    }

    // Get the number of dice
    public int getNumberOfDice() {
        return numberOfDice;
    }

    // Get the values of individual dice
    public List<String> getDiceValues() {
        return new ArrayList<>(diceValues);
    }

    // Setter for changing the number of dice
    public void setNumberOfDice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
    }

    // Create mapping from dice values to Dudo values
    private Map<Integer, String> createDiceMapping() {
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(1, "ace");
        mapping.put(2, "king");
        mapping.put(3, "queen");
        mapping.put(4, "jack");
        mapping.put(5, "9");
        mapping.put(6, "10");
        return mapping;
    }
}
