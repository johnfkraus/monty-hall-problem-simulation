package com.johnfkraus;

import java.util.*;

/**
 * The type Game (Let's Make a Deal).
 */
public class Game {
    /**
     * The enum Door.
     */
    enum Door {
        /**
         * Door number one.
         */
        ONE(1),
        /**
         * Door number two.
         */
        TWO(2),
        /**
         * Door number three.
         */
        THREE(3);
        /**
         * The Door number.
         */
        public final int doorNumber;

        Door(int doorNumber) {
            this.doorNumber = doorNumber;
        }
    }

    /**
     * The number of games instantiated.
     */
    static int gameNumber;
    /**
     * The the count of how many times not switching led to a win.
     */
    static int stayWinsCount;
    /**
     * The the count of how many times switching led to a win.
     */
    static int switchWinsCount;

    /**
     * There are three closed doors from which the contestant may choose.  Behind one door (the winning door, selected randomly) is the prize: a new car.  Behind the other two doors are goats.  We assume the contestant wants to win a car and doesn't want a goat.
     */
    static Door[] doorArr = {Door.ONE, Door.TWO, Door.THREE};
    /**
     * The Showable Door list.  The list from which the game show host, Monty, can choose a door to open for the contestant when offering contestant the opportunity to switch doors.  The door Monty opens before inviting the contestant to switch must be (1) a door behind which is a goat (not the winning door); and (2) not the door initially chosen by the contestant.
     */
    List<Door> showableDoorList = new ArrayList<>(Arrays.asList(doorArr));
    /**
     * The Winning door. The winning door is chosen by a pseudo random process.  There is a new car behind this door; behind each of the other
     * two doors are goats.  No one ever wanted one of the goats.  But if a contestant had won a goat in
     * on Let's Make a Deal in the 1960s they might still have a nice little herd of goats.  If a contestant won a
     * 1960s-era automobile, if the unsafe car of that era didn't kill you it would by now be a worthless rusting heap.
     * What ever happened to those goats, anyway?
     */
    Door winningDoor;
    /**
     * The Picked door.   The contestant picks a door.  The selection is simulated by a pseudo random process.
     */
    Door pickedDoor;
    /**
     * The Shown door.  After the contestant chooses a door, Monty (always, we assume, perhaps unrealistically) opens a door behind which is a goat.  If the contestant's first choice of door (unbeknownst to the contestant) has a car hidden behind it, Monty will open one of the two goat doors selected at random.  If the contestant's first door choice was a goat door, Monty will open the other goat door, not the winning door.  On the actual TV show, Monty did not always offer the contestant a chance to switch doors, reports say.  See Wikipedia.
     */
    Door shownDoor;
    /**
     * The Switch door.  The contestant has picked one door.  Monty has opened a different door to reveal a goat.  There is one remaining door to which the contestant can opt to switch, forsaking the contestant's originally selected door.
     */
    Door switchDoor;
    /**
     * The Original choice wins counter.
     */
    boolean originalChoiceWins;
    /**
     * The Switch wins counter.
     */
    boolean switchWins;

    /**
     * Instantiates and completes a single Game.  The winning and picked doors are randomly selected; they can be the same door or different doors.
     */
    Game() {
        gameNumber++;
        winningDoor = pickRandomDoor();
        pickedDoor = pickRandomDoor();
        showableDoorList.remove(winningDoor);
        showableDoorList.remove(pickedDoor);
        shownDoor = pickShownDoor(showableDoorList); // After contestant picks a door we assume Monty always opens a goat door and give the contestant the opportunity to switch to the other unopened door.  On the TV game show, however, Monty did not always give the contestant the chance to make such a switch.
        switchDoor = pickSwitchDoor();

        if (winningDoor == pickedDoor) {
            originalChoiceWins = true;
            stayWinsCount++;
        } else if (winningDoor == switchDoor) {
            switchWins = true;
            switchWinsCount++;
        } else {
            throw new IllegalStateException("Program logic error, apparently.");
        }
        repInvariant(); // check for mistakes.
    }

    private void repInvariant() {
        // check for disallowed conditions
        if (originalChoiceWins == switchWins) {
            throw new IllegalStateException("Only one strategy, switching or not switching, can win.  There should be only one winning door.  After Monty opens one of the goat doors, there are only two closed doors for the contestant to choose from: the contestant's originally-selected door or the other door.  The contestant might choose the winning door by sticking with their original choice or by switching but not both.");
        }
        if (shownDoor == winningDoor) {
            throw new IllegalStateException("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors.  Instead, Monty must show a goat door.");
        }
        if (shownDoor == pickedDoor) {
            throw new IllegalStateException("Monty must not open the door initially picked by the contestant until contestant has decided whether or not to switch doors.");
        }
        if (shownDoor == switchDoor) {
            throw new IllegalStateException("It is assumed that the contestant would not logically choose to switch to the door which Monty has already opened to reveal a goat.");
        }
    }

    private Door pickRandomDoor() {
        int randomNumber = (new Random()).nextInt(doorArr.length);
        return doorArr[randomNumber];
    }

    // Pick the (goat) door that Monty will open before offering to let contestant switch doors; If Monty can choose from two goat doors, he chooses one of the two randomly.
    private Door pickShownDoor(List<Door> doorList) {
        int randomNumber = (new Random()).nextInt(doorList.size());
        return doorList.get(randomNumber);
    }

    // To which door can the contestant switch?  It's the one door left after you take away the contestant's first choice door and the goat door Monty opened.
    private Door pickSwitchDoor() {
        Set<Door> allDoors = new HashSet<>(Arrays.asList(doorArr));
        allDoors.remove(pickedDoor); // Contestant can't switch to the door already chosen. That wouldn't be switching.
        allDoors.remove(shownDoor); // Contestant can't/won't switch to the goat door Monty has opened since the contestant instead wants the new car behind winning door.
        if (allDoors.size() != 1) {
            throw new IllegalStateException("There should be only one door available in the Set allDoors to which contestant may choose to switch.  allDoors.size() = " + allDoors.size());
        }
        // Return the one and only door remaining.
        return allDoors.iterator().next();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game #")
            .append(gameNumber)
            .append(", winningDoor = ")
            .append(winningDoor)
            .append(", pickedDoor = ")
            .append(pickedDoor)
            .append(", shownDoor ")
            .append(shownDoor)
            .append(", switchDoor = ")
            .append(switchDoor)
            .append(winningDoor == switchDoor ? ", switching wins" : "");
        return sb.toString();
    }
}
