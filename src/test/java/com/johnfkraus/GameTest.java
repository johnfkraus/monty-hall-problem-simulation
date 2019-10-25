package com.johnfkraus;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

public class GameTest {

    public Game game;

    @Before
    public void setup() {
        game = new Game();
    }

    @Test
    public void gameRepInvariant() {
        // game = new Game();
        assertEquals(3, game.doorArr.length);
        assertTrue(game.doorList.size() <= 2);
        assertTrue(game.doorList.size() >= 1);
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
    @Test
    public void thereWillBeOneDoorToSwitchTo() {
        Set<Game.Door> allDoors = new HashSet<>(Arrays.asList(Game.doorArr));
        allDoors.remove(game.pickedDoor); // Contestant can't switch to the door already chosen. That wouldn't be switching.
        allDoors.remove(game.shownDoor); // Contestant can't/won't switch to the goat door Monty has opened
        // there should be only one door left in the allDoors Set, the door to which the contestant might choose to switch.
        assertEquals(allDoors.size(), 1);
    }
}
