package com.johnfkraus;

/**
 * Class to run multiple iterations of Let's Make A Deal Game
 */
class MontyHallProblemSimulationRunner {
    private int stayWins = 0;
    private int switchWins = 0;
    private int numberOfGamesToPlay = 1000;

    private void run() {
        while(Game.gameNumber < numberOfGamesToPlay) {
            Game game = new Game();
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
        // System.out.println("stayWins = " + stayWins + ", switchWins = " + switchWins + ", #games = " + Game.gameNumber);
        System.out.println("stayWinsCount = " + Game.stayWinsCount + ", switchWinsCount = " + Game.switchWinsCount + ", #games = " + Game.gameNumber);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
        MontyHallProblemSimulationRunner mh = new MontyHallProblemSimulationRunner();
        mh.run();
    }
}


