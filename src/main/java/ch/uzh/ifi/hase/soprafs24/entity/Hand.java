package ch.uzh.ifi.hase.soprafs24.entity;

public class Hand {

    private Dice[] dices = new Dice[5];

    public Hand() {
        for (int i = 0; i < 5; i++) {
            dices[i] = new Dice();
        }
    }

    public void roll() {
        for (Dice dice : dices) {
            dice.roll();
        }
    }

    public Dice[] getDices() {
        return dices;
    }

    public Long countSuit(Suit suit){
        Long count = 0L;
        for (Dice dice : dices) {
            if (dice.getSuit() == suit) {
                count++;
            }
        }
        return count;
    }

}
