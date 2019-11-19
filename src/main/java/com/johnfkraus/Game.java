package com.johnfkraus;

import java.util.*;

public class Game {
    enum Door {
        ONE(1), TWO(2), THREE(3);
        public final int doorNumber;
        Door(int doorNumber) {
            this.doorNumber = doorNumber;
        }
    }

    static int gameNumber;
    static int stayWinsCount;
    static int switchWinsCount;

    /**
     * There are three closed doors from which the contestant may choose.  Behind one door (the winning door) is the prize: a new car.  Behind the other two doors are goats.  We assume the contestant wants to win a car and doesn't want a goat.
     */
    static Door[] doorArr = {Door.ONE, Door.TWO, Door.THREE};
    // List<Door> allDoors = new ArrayList<>(Arrays.asList(doorArr));
    List<Door> doorList = new ArrayList<>(Arrays.asList(doorArr));
    //List<Door> doorList = new ArrayList<>(allDoors);
    // { System.out.println((this.allDoors == this.doorList)); }
    Door winningDoor; // The winning door is chosen at random.  There is a new car behind this door; behind the other two doors are goats. Is it a little sad that no one ever wanted one of the goats?  If you had won a goat in on Let's Make a Deal in the 1960s you might still have a nice little herd of goats.  But if you won a 1960s-era car, if it didn't kill you it would now be a worthless rusting heap. What ever happened to those goats, anyway?
    Door pickedDoor; // The contestant picks a door.
    Door shownDoor; // After the contestant chooses a door, Monty (always, we assume, perhaps unrealistically) opens a door behind which is a goat.  If the contestant's first choice of door (unbeknownst to the contestant) has a car hidden behind it, Monty will open one of the two goat doors selected at random.  If the contestant's first door choice was a goat door, Monty will open the other goat door, not the winning door.  On the actual TV show, Monty did not always offer the contestant a chance to switch doors, reports say.  See Wikipedia.
    Door switchDoor; // The contestant has picked one door.  Monty has opened a different door to reveal a goat.  There is one remaining door to which the contestant can opt to switch, forsaking the contestant's originally selected door.
    boolean originalChoiceWins;
    boolean switchWins;

    // the winning and picked doors are randomly selected; they can be the same door or different doors
    Game() {
        gameNumber++;
        winningDoor = pickRandomDoor();
        pickedDoor = pickRandomDoor();
        doorList.remove(winningDoor);
        doorList.remove(pickedDoor);
        shownDoor = pickShownDoor(doorList); // After contestant picks a door we assume Monty always opens a goat door and give the contestant the opportunity to switch to the other unopened door.  On the TV game show, however, Monty did not always give the contestant the chance to make such a switch.
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
            throw new IllegalStateException("There is only one winning door.  After Monty opens one of the goat doors, there are only two choices for the contestant: stay or switch.  The contestant might choose the winning door by sticking with their original choice or by switching but not both.");
        }
        if (shownDoor == winningDoor) {
            throw new IllegalStateException("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors.");
        }
        if (shownDoor == pickedDoor) {
            throw new IllegalStateException("Monty must not show the door initially picked by the contestant until contestant has a chance to switch doors.");
        }
        if (shownDoor == switchDoor) {
            throw new IllegalStateException("Contestant would not logically choose the opened door behind which a goat has already been revealed.");
        }
    }

    private Door pickRandomDoor() {
        int randomNumber = (new Random()).nextInt(doorArr.length);
        return doorArr[randomNumber];
    }

    // pick the (goat) door that Monty will open before offering to let contestant switch doors; If Monty can choose from two goat doors, he chooses one of the two randomly
    private Door pickShownDoor(List<Door> doorList) {
        int randomNumber = (new Random()).nextInt(doorList.size());
        return doorList.get(randomNumber);
    }

    // To which door can the contestant switch?  It's the door left after you take away the contestant's first choice door and the goat door Monty opened.
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
