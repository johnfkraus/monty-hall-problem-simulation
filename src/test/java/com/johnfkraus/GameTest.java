package com.johnfkraus;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

public class GameTest {

    @Test
    public void gameRepInvariant() {
        Game game = new Game();
        assertEquals(3, game.doorArr.length);
        assertEquals(true, game.doorList.size() <= 2);
        assertEquals(true, game.doorList.size() >= 1);
        assertFalse(game.doorList.contains(game.pickedDoor));
        assertFalse(game.doorList.contains(game.winningDoor));
        // check for disallowed conditions
        // When offering the contestant the chance to switch doors, Monty must not show the winning door
        assertNotSame(game.shownDoor, game.winningDoor);
        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        assertNotSame(game.shownDoor, game.pickedDoor);
        // Contestant would not logically choose the opened door behind which a goat has already been revealed.
        assertNotSame(game.shownDoor, game.switchDoor);
    }
}
