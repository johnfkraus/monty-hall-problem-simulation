package com.johnfkraus;

import java.util.ArrayList;
import java.util.List;

class MontyHallProblemSimulation {
    // all game objects will be stored in an ArrayList for printing results later
    private List<Game> games = new ArrayList<>();
    private int stayWins = 0;
    private int switchWins = 0;

    private void run() {
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
            // System.out.print("Game #" + (i + 1) + ", ");
            System.out.println(game.toString());
        }
        // final results of all games
        System.out.println("stayWins = " + stayWins + ", switchWins = " + switchWins + ", #games = " + games.size());
    }

    public static void main(String... args) {
        MontyHallProblemSimulation mh = new MontyHallProblemSimulation();
        mh.run();
    }
}


