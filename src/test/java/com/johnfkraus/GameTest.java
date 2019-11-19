package com.johnfkraus;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

/**
 * The type Game test.
 */
public class GameTest {

    /**
     * The Game.
     */
    public Game game;

    /**
     * Create a Game instance and assign the instance to the game reference.
     */
    @Before
    public void setup() {
        game = new Game();
    }

    /**
     * Game rep invariant.  Checking required and disallowed conditions.
     */
    @Test
    public void gameRepInvariant() {
        assertEquals(3, game.doorArr.length);
        assertTrue(game.showableDoorList.size() <= 2);
        assertTrue(game.showableDoorList.size() >= 1);
        assertFalse(game.showableDoorList.contains(game.pickedDoor));
        assertFalse(game.showableDoorList.contains(game.winningDoor));
        // check for disallowed conditions
        // When offering the contestant the chance to switch doors, Monty must not show the winning door
        assertNotSame(game.shownDoor, game.winningDoor);
        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        assertNotSame(game.shownDoor, game.pickedDoor);
        // Contestant would not logically choose the opened door behind which a goat has already been revealed.
        assertNotSame(game.shownDoor, game.switchDoor);
    }

    /**
     * There will be one door to switch to.
     */
    @Test
    public void thereWillBeOneDoorToSwitchTo() {
        Set<Game.Door> allDoors = new HashSet<>(Arrays.asList(Game.doorArr));
        allDoors.remove(game.pickedDoor); // Contestant can't switch to the door already chosen. That wouldn't be switching.
        allDoors.remove(game.shownDoor); // Contestant can't/won't switch to the goat door Monty has opened
        // there should be only one door left in the allDoors Set, the door to which the contestant might choose to switch.
        assertEquals(allDoors.size(), 1);
    }
}
