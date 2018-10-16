package com.company;

import javafx.scene.control.TreeCell;

import java.util.*;

public class PokerHand {
    public enum Result {TIE, WIN, LOSS}

    public enum Combination {
        StraightFlush(9), FourOfAKind(8), FullHouse(7), Flush(6), Straight(5), ThreeOfAKind(4), TwoPair(3), OnePair(2), Highest(1);

        int value;

        Combination(int p) {
            value = p;
        }

        int showValue() {
            return value;
        }
    }


    String stringPlayerHand;
    List<Card> playerHand;
    Combination playerResult;
    boolean isFlush = false;
    boolean isStraight = false;
    boolean isFullHouse = false;
    boolean isFourOfAKind = false;
    boolean isThreeOfAKind = false;
    boolean isTwoPair = false;
    boolean isOnePair = false;
    int firstPairRank = 0;
    int secondPairRank = 0;
    int threeOfAKindRank = 0;
    int fourOfAKindRank = 0;


    Map<String, String> straightMap;


    public PokerHand(String hand) {
        this.stringPlayerHand = hand;
        playerHand = new ArrayList<>();
        parseStringToCard();
        checkStraight();
        checkFlush();
        checkNormalCombinations();
        rankHandResult();
    }

    private void parseStringToCard() {
        String[] cardsAsStringArray;
        cardsAsStringArray = this.stringPlayerHand.split(" ");
        for (String stringCard : cardsAsStringArray) {
            Card actualCard = new Card(stringCard);
            playerHand.add(actualCard);
        }
        Collections.sort(this.playerHand);
    }

    private void createStraightMap() {
        straightMap = new HashMap<String, String>();
        straightMap.put("A", "2");
        straightMap.put("2", "3");
        straightMap.put("3", "4");
        straightMap.put("4", "5");
        straightMap.put("5", "6");
        straightMap.put("6", "7");
        straightMap.put("7", "8");
        straightMap.put("8", "9");
        straightMap.put("9", "T");
        straightMap.put("T", "J");
        straightMap.put("J", "Q");
        straightMap.put("Q", "K");
        straightMap.put("K", "A");
    }

    private void checkStraight() {
        createStraightMap();
        Boolean straightFlag = false;
        int index = 0;

        for (int z = 0; z < this.playerHand.size(); z++) {
            index = z + 1;
            while (index < 5) {
                if (straightMap.get(this.playerHand.get(z).stringRank).equals(this.playerHand.get(index).stringRank)) {
                    straightFlag = true;
                    break;
                } else {
                    straightFlag = false;
                    break;
                }
            }
            if (!straightFlag) {
                break;
            }
        }
        if (straightFlag) {
            this.isStraight = true;
        }
    }

    private void checkFlush() {
        Boolean flushFlag = false;
        int index = 0;
        for (int z = 0; z < this.playerHand.size(); z++) {
            while (index < 5) {
                if (this.playerHand.get(z).getSuit().equals(this.playerHand.get(index).getSuit())) {
                    flushFlag = true;
                    break;
                } else {
                    flushFlag = false;
                    break;
                }
            }
            if (!flushFlag) {
                break;
            }
        }
        if (flushFlag) {
            this.isFlush = true;
        }
    }


    private void checkNormalCombinations() {
        int matchesRank = 0;
        int foundCombinationRank = 0;
        int matchesSuit = 0;
        int index;

        for (int i = 0; i < this.playerHand.size(); i++) {
            index = i + 1;

            while (index < 5) {
                if (this.isThreeOfAKind) {
                    if (this.playerHand.get(i - 1).rank.equals(this.playerHand.get(i).rank)) {
                        break;
                    }
                }
                if (this.playerHand.get(i).rank.equals(this.playerHand.get(index).rank)) {
                    matchesRank++;
                    foundCombinationRank = this.playerHand.get(i).rank.value;
                }
                index++;
            }
            //FOUR-OF-A-KIND
            if (matchesRank == 3) {
                this.isFourOfAKind = true;
                this.fourOfAKindRank = foundCombinationRank;
                break;
            } else if (matchesRank == 2) {
                this.isThreeOfAKind = true;
                this.threeOfAKindRank = foundCombinationRank;
            } else if (matchesRank == 1) {
                if (this.isOnePair == true) {
                    this.isOnePair = false;
                    this.isTwoPair = true;
                    this.secondPairRank = foundCombinationRank;
                } else {
                    this.isOnePair = true;
                    this.firstPairRank = foundCombinationRank;
                }
            }
            foundCombinationRank = 0;
            matchesRank = 0;
            matchesSuit = 0;
        }
    }

    private void rankHandResult() {

        if (this.isStraight && this.isFlush) {
            this.playerResult = Combination.StraightFlush;
        } else if (this.isFourOfAKind) {
            this.playerResult = Combination.FourOfAKind;
        } else if (this.isThreeOfAKind && this.isOnePair) {
            this.playerResult = Combination.FullHouse;
        } else if (this.isFlush) {
            this.playerResult = Combination.Flush;
        } else if (this.isStraight) {
            this.playerResult = Combination.Straight;
        } else if (this.isThreeOfAKind) {
            this.playerResult = Combination.ThreeOfAKind;
        } else if (this.isTwoPair) {
            this.playerResult = Combination.TwoPair;
        } else if (this.isOnePair) {
            this.playerResult = Combination.OnePair;
        } else {
            this.playerResult = Combination.Highest;
        }
    }


    public Result compareWith(PokerHand opponentHand) {
        Result playerResult;
        Result opponentResult;

        if (this.playerResult.value > opponentHand.playerResult.value) {
            playerResult = Result.WIN;
            opponentResult = Result.LOSS;
        } else if (this.playerResult.value < opponentHand.playerResult.value) {
            playerResult = Result.LOSS;
            opponentResult = Result.WIN;
        } else {
            playerResult = drawHandle(opponentHand);
        }

        if (playerResult.equals(Result.LOSS)) {
            return Result.LOSS;
        } else if (playerResult.equals(Result.WIN)) {
            return Result.WIN;
        }

        return Result.TIE;

    }

    private Result drawHandle(PokerHand opponentHand) {
        Result result = Result.TIE;
        int a = 2;

        if (this.playerResult.equals(Combination.StraightFlush)) { //Straight Flush Draw
            if (this.playerHand.get(4).rank.value > opponentHand.playerHand.get(4).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(4).rank.value < opponentHand.playerHand.get(4).rank.value) {
                result = Result.LOSS;
            } else {
                result = Result.TIE;
            }
        } else if (this.playerResult.equals(Combination.FourOfAKind)) { //Four Of a Kind Draw
            if (this.fourOfAKindRank > opponentHand.fourOfAKindRank) {
                result = Result.WIN;
            } else if (this.fourOfAKindRank < opponentHand.fourOfAKindRank) {
                result = Result.LOSS;
            } else {
                for(int k = 0; k < this.playerHand.size(); k++){
                    if(this.playerHand.get(k).rank.value == this.fourOfAKindRank){
                        this.playerHand.remove(k);
                        k--;
                    }
                }
                for(int l = 0; l < opponentHand.playerHand.size(); l++){
                    if(opponentHand.playerHand.get(l).rank.value == opponentHand.fourOfAKindRank  ){
                        opponentHand.playerHand.remove(l);
                        l--;
                    }
                }
                if(this.playerHand.get(0).rank.value > opponentHand.playerHand.get(0).rank.value){
                    result = Result.WIN;
                } else if(this.playerHand.get(0).rank.value < opponentHand.playerHand.get(0).rank.value){
                    result = Result.LOSS;
                } else{
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.FullHouse)) { //FullHouse Draw
            if (this.threeOfAKindRank > opponentHand.threeOfAKindRank) {
                result = Result.WIN;
            } else if (this.threeOfAKindRank < opponentHand.threeOfAKindRank) {
                result = Result.LOSS;
            } else {
                for(int k = 0; k < this.playerHand.size(); k++){
                    if(this.playerHand.get(k).rank.value == this.threeOfAKindRank){
                        this.playerHand.remove(k);
                        k--;
                    }
                }
                for(int l = 0; l < opponentHand.playerHand.size(); l++){
                    if(opponentHand.playerHand.get(l).rank.value == opponentHand.threeOfAKindRank  ){
                        opponentHand.playerHand.remove(l);
                        l--;
                    }
                }
                if(this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value > opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value){
                    result = Result.WIN;
                } else if(this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value <  opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value){
                    result = Result.LOSS;
                } else{
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.Flush)) { //Flush Draw
            if (this.playerHand.get(4).rank.value > opponentHand.playerHand.get(4).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(4).rank.value < opponentHand.playerHand.get(4).rank.value) {
                result = Result.LOSS;
            } else {
                if (this.playerHand.get(4).rank.value + this.playerHand.get(3).rank.value + this.playerHand.get(2).rank.value + this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value > opponentHand.playerHand.get(4).rank.value + opponentHand.playerHand.get(3).rank.value +opponentHand.playerHand.get(2).rank.value + opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value) {
                    result = Result.WIN;
                } else if (this.playerHand.get(4).rank.value + this.playerHand.get(3).rank.value + this.playerHand.get(2).rank.value + this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value < opponentHand.playerHand.get(4).rank.value + opponentHand.playerHand.get(3).rank.value  +  opponentHand.playerHand.get(2).rank.value + opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value) {
                    result = Result.LOSS;
                } else {
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.Straight)) { //Straight Draw
            if (this.playerHand.get(4).rank.value > opponentHand.playerHand.get(4).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(4).rank.value < opponentHand.playerHand.get(4).rank.value) {
                result = Result.LOSS;
            } else {
                result = Result.TIE;
            }
        } else if (this.playerResult.equals(Combination.ThreeOfAKind)) { //Three Of a Kind Draw
            if (this.threeOfAKindRank > opponentHand.threeOfAKindRank) {
                result = Result.WIN;
            } else if (this.threeOfAKindRank < opponentHand.threeOfAKindRank) {
                result = Result.LOSS;
            } else {
                for(int k = 0; k < this.playerHand.size(); k++){
                    if(this.playerHand.get(k).rank.value == this.threeOfAKindRank){
                        this.playerHand.remove(k);
                        k--;
                    }
                }
                for(int l = 0; l < opponentHand.playerHand.size(); l++){
                    if(opponentHand.playerHand.get(l).rank.value == opponentHand.threeOfAKindRank  ){
                        opponentHand.playerHand.remove(l);
                        l--;
                    }
                }
                if(this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value > opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value){
                    result = Result.WIN;
                } else if(this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value <  opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value){
                    result = Result.LOSS;
                } else{
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.TwoPair)) { //Two Pair Draw
            if (this.secondPairRank > opponentHand.secondPairRank) {
                result = Result.WIN;
            } else if (this.secondPairRank < opponentHand.secondPairRank) {
                result = Result.LOSS;
            } else {
                if (this.firstPairRank > opponentHand.firstPairRank) {
                    result = Result.WIN;
                } else if (this.firstPairRank < opponentHand.firstPairRank) {
                    result = Result.LOSS;
                } else {
                    for(int k = 0; k < this.playerHand.size(); k++){
                        if(this.playerHand.get(k).rank.value == this.firstPairRank || this.playerHand.get(k).rank.value == this.secondPairRank ){
                            this.playerHand.remove(k);
                            k--;
                        }
                    }
                    for(int l = 0; l < opponentHand.playerHand.size(); l++){
                        if(opponentHand.playerHand.get(l).rank.value == opponentHand.firstPairRank || opponentHand.playerHand.get(l).rank.value == opponentHand.secondPairRank ){
                            opponentHand.playerHand.remove(l);
                            l--;
                        }
                    }
                    if(this.playerHand.get(0).rank.value > opponentHand.playerHand.get(0).rank.value){
                        result = Result.WIN;
                    } else if(this.playerHand.get(0).rank.value < opponentHand.playerHand.get(0).rank.value){
                        result = Result.LOSS;
                    } else{
                        result = Result.TIE;
                    }
                }
            }
        } else if (this.playerResult.equals(Combination.OnePair)) { //One Pair Draw
            if (this.firstPairRank > opponentHand.firstPairRank) {
                result = Result.WIN;
            } else if (this.firstPairRank < opponentHand.firstPairRank) {
                result = Result.LOSS;
            } else {
                for (int z = 0; z < this.playerHand.size(); z++) {
                    if (this.playerHand.get(z).rank.value == this.firstPairRank) {
                        this.playerHand.remove(z);

                        z--;
                    }
                }
                for (int y = 0; y < opponentHand.playerHand.size(); y++) {
                    if (opponentHand.playerHand.get(y).rank.value == this.firstPairRank) {
                        opponentHand.playerHand.remove(y);
                        y--;
                    }
                }
                if (this.playerHand.get(2).rank.value + this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value > opponentHand.playerHand.get(2).rank.value + opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value) {
                    result = Result.WIN;
                } else if (this.playerHand.get(2).rank.value + this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value < opponentHand.playerHand.get(2).rank.value + opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value) {
                    result = Result.LOSS;
                } else {
                    result = Result.TIE;
                }
            }
        } else if (this.playerResult.equals(Combination.Highest)) { //Highest Draw
            if (this.playerHand.get(4).rank.value > opponentHand.playerHand.get(4).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(4).rank.value < opponentHand.playerHand.get(4).rank.value) {
                result = Result.LOSS;
            } else if (this.playerHand.get(3).rank.value > opponentHand.playerHand.get(3).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(3).rank.value < opponentHand.playerHand.get(3).rank.value) {
                result = Result.LOSS;
            }  else if (this.playerHand.get(2).rank.value > opponentHand.playerHand.get(2).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(2).rank.value < opponentHand.playerHand.get(2).rank.value) {
                result = Result.LOSS;
            } else if (this.playerHand.get(1).rank.value > opponentHand.playerHand.get(1).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(1).rank.value < opponentHand.playerHand.get(1).rank.value) {
                result = Result.LOSS;
            } else if (this.playerHand.get(0).rank.value > opponentHand.playerHand.get(0).rank.value) {
                result = Result.WIN;
            } else if (this.playerHand.get(0).rank.value < opponentHand.playerHand.get(0).rank.value) {
                result = Result.LOSS;
            }
            else {
                if (this.playerHand.get(4).rank.value + this.playerHand.get(3).rank.value + this.playerHand.get(2).rank.value + this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value > opponentHand.playerHand.get(4).rank.value + opponentHand.playerHand.get(3).rank.value +opponentHand.playerHand.get(2).rank.value + opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value) {
                    result = Result.WIN;
                } else if (this.playerHand.get(4).rank.value + this.playerHand.get(3).rank.value + this.playerHand.get(2).rank.value + this.playerHand.get(1).rank.value + this.playerHand.get(0).rank.value < opponentHand.playerHand.get(4).rank.value + opponentHand.playerHand.get(3).rank.value  +  opponentHand.playerHand.get(2).rank.value + opponentHand.playerHand.get(1).rank.value + opponentHand.playerHand.get(0).rank.value) {
                    result = Result.LOSS;
                } else {
                    result = Result.TIE;
                }
            }
        }


        return result;
    }


}





