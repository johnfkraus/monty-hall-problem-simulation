package com.johnfkraus;

class MontyHallProblemSimulation {
    // all game objects will be stored in an ArrayList for printing results later NOT!
    // private List<Game> games = new ArrayList<>();
    private int stayWins = 0;
    private int switchWins = 0;
    private int gamesPlayed;

    private void run() {
        while(Game.gameNumber < 1000) {
            Game game = new Game();
            gamesPlayed++;
            if (game.originalChoiceWins) {
                stayWins++;
            } else if (game.switchWins) {
                switchWins++;
            } else {
                throw new RuntimeException("One and only one of the two strategies, switch or stay, must win.");
            }
            System.out.println(game.toString());
        }
        // final results of all games
        System.out.println("stayWins = " + stayWins + ", switchWins = " + switchWins + ", #games = " + gamesPlayed);
    }

    public static void main(String... args) {
        MontyHallProblemSimulation mh = new MontyHallProblemSimulation();
        mh.run();
    }
}


