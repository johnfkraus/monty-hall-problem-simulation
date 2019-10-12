package com.johnfkraus;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GameTest {

    @Test
    public void gameRepInvariant() throws Exception {
        Game game = new Game();
        //void testResults() throws Exception {
        // check for disallowed conditions
        // When offering the contestant the chance to switch doors, Monty must not show the winning door
        assertTrue(game.shownDoor != game.winningDoor);

        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        assertTrue(game.shownDoor != game.pickedDoor);
        // Contestant would not logically choose the opened door behind which a goat has already been revealed.
        assertTrue(game.shownDoor != game.switchDoor);
    }
}
