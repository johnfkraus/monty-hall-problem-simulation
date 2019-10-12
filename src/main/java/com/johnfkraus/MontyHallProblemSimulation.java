package com.johnfkraus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

enum Door {
    ONE, TWO, THREE
}

class MontyHallProblemSimulation {
    // all game objects will be stored in an ArrayList for printing results later
    List<Game> games = new ArrayList<>();
    int stayWins = 0;
    int switchWins = 0;

    void run() throws Exception {
        int runs = 1000;
        for (int i = 0; i < runs; i++) {
            Game game = new Game();
            if (game.originalChoiceWins) {
                stayWins++;
            }
            if (game.switchWins) {
                switchWins++;
            }
            games.add(game); // save for possible further analysis
            System.out.print("Game #" + (i + 1) + ", ");
            System.out.println(game.toString());
        }
        // final results of all games
        System.out.println("stayWins = " + stayWins + ", switchWins = " + switchWins + ", #games = " + games.size());
    }

    public static void main(String... args) throws Exception {
        MontyHallProblemSimulation mh = new MontyHallProblemSimulation();
        mh.run();
    }
}


