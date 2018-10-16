package com.company;


public class Card implements Comparable<Card> {

    String stringRank;
    String suit;
    Rank rank;

    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
        int value;

        Rank(int p) {
            value = p;
        }

        int showValue() {
            return value;
        }

    }

    @Override
    public int compareTo(Card card2) {

        if (this.rank.value < card2.rank.value) {
            return -1;
        }

        if (this.rank.value > card2.rank.value) {
            return 1;
        }
        return 0;

    }

    public void matchRank() {
        if (this.stringRank.equals("2")) {
            this.rank = Rank.TWO;
        } else if (this.stringRank.equals("3")) {
            this.rank = Rank.THREE;
        } else if (this.stringRank.equals("4")) {
            this.rank = Rank.FOUR;
        } else if (this.stringRank.equals("5")) {
            this.rank = Rank.FIVE;
        } else if (this.stringRank.equals("6")) {
            this.rank = Rank.SIX;
        } else if (this.stringRank.equals("7")) {
            this.rank = Rank.SEVEN;
        } else if (this.stringRank.equals("8")) {
            this.rank = Rank.EIGHT;
        } else if (this.stringRank.equals("9")) {
            this.rank = Rank.NINE;
        } else if (this.stringRank.equals("T")) {
            this.rank = Rank.TEN;
        } else if (this.stringRank.equals("J")) {
            this.rank = Rank.JACK;
        } else if (this.stringRank.equals("Q")) {
            this.rank = Rank.QUEEN;
        } else if (this.stringRank.equals("K")) {
            this.rank = Rank.KING;
        } else if (this.stringRank.equals("A")) {
            this.rank = Rank.ACE;
        }
    }


    public Card(String card) {
        String[] matchCard;
        matchCard = card.split("");
        this.stringRank = matchCard[0];
        this.suit = matchCard[1];
        matchRank();

    }

    public String getStringRank() {
        return stringRank;
    }

    public void setStringRank(String stringRank) {
        this.stringRank = stringRank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Card{" +
                "stringRank='" + stringRank + '\'' +
                ", suit='" + suit + '\'' +
                ", rank=" + rank +
                '}';
    }
}