package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        String stringTest = "5/ 4/ 3/ 2/ 1/ 0/ X 9/ 4/ 7/2";

        PokerHand firstPlayer = new PokerHand("TS KS 5S 9S AC");
        PokerHand opponent = new PokerHand("JH 8S TH AH QH");
        firstPlayer.compareWith(opponent);

        int a = 2;
    }



}



