package com.johnfkraus;

import java.util.*;

public class Game {
    static Door[] doorArr = {Door.ONE, Door.TWO, Door.THREE}; // there are three doors from which to choose.
    Door[] doorsMontyCanShow = doorArr.clone();
    List<Door> doorList = new ArrayList<Door>(Arrays.asList(doorArr)); // {Door.ONE, Door.TWO, Door.THREE}));
    Door winningDoor = null; // the winning door, chosen at random; there is a new car behind this door; behind the other two doors are goats. Is it a little sad that no one ever wanted one of the goats?  If you had won a goat in on Let's Make a Deal in the 1960s you might still have a nice little herd of goats.  But if you won a 1960s-era car, if it didn't kill you it would for sure now be worthless and rusting away in some dump.  What ever happened to those goats, anyway?
    Door pickedDoor = null;
    Door shownDoor = null; // After the contestant chooses a door, Monty (always, we assume, perhaps unrealistically) opens a door behind which is a goat.  If the contestant's first choice was the door with the car, Monty will choose one of the two goat doors at random.
    Door switchDoor; // the remaining door to which the contestant can opt to switch.
    boolean originalChoiceWins;
    boolean switchWins;

    // the winning and picked doors are randomly selected; they can be the same door or different doors
    Game() throws Exception {
        winningDoor = pickRandomDoor();
        pickedDoor = pickRandomDoor();
        doorList.remove(winningDoor);
        doorList.remove(pickedDoor);
        shownDoor = pickShownDoor(doorList); // After contestant picks a door Monty is assumed to always open a goat door and give the contestant the opportunity to switch to the other unopened door.  In reality, Monty did not always give the contestant the chance to make this switch.
        switchDoor = pickSwitchDoor();
        testResults(); // make sure Monty or the contestant didn't make a mistake
        if (winningDoor == pickedDoor) {
            originalChoiceWins = true;
        } else if (winningDoor == switchDoor) {
            switchWins = true;
        } else {
            throw new Exception("Program logic error, apparently.");
        }
    }

    void testResults() throws Exception {
        // check for disallowed conditions
        // When offering the contestant the chance to switch doors, Monty must not show the winning door
        if (shownDoor == winningDoor) {
            throw new Exception("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors.");
        }
        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        if (shownDoor == pickedDoor) {
            throw new Exception("Monty must not show the door initially picked by the contestant until contestant has a chance to switch doors.");
        }
        if (shownDoor == switchDoor) {
            throw new Exception("Contestant would not logically choose the opened door behind which a goat has already been revealed.");
        }
    }

    static Door pickRandomDoor() {
        Random r = new Random();
        int randomNumber = r.nextInt(doorArr.length);
        Door pickedDoor = doorArr[randomNumber];
        return pickedDoor;
    }


    // pick the goat door that Monty will open before offering to let contestant switch doors;
    // pick the (goat) door that Monty will open before offering to let contestant switch doors; If Monty can choose from two goat doors, he chooses one of the two randomly
    static Door pickShownDoor(List<Door> doorList) {
        Random r = new Random();
        int randomNumber = r.nextInt(doorList.size());
        return doorList.get(randomNumber);
    }

    // To which door can the contestant switch?  It's the door left after you take away the contestant's first choice door and the goat door Monty opened.
    Door pickSwitchDoor() throws Exception {
        Set<Door> allDoors = new HashSet<>();
        allDoors.add(Door.ONE);
        allDoors.add(Door.TWO);
        allDoors.add(Door.THREE);
        Set<Door> nonSwitchDoors = new HashSet<Door>();
        nonSwitchDoors.add(pickedDoor); // Contestant can't switch to the door already chosen.
        nonSwitchDoors.add(shownDoor); // Contestant can't/won't switch to the goat door Monty has opened
        allDoors.removeAll(nonSwitchDoors);
        // there should be only one door left in the allDoors Set.
        if (allDoors.size() != 1) {
            throw new Exception("There should be only one door available to which contestant can switch.");
        }
        return allDoors.iterator().next();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("winningDoor = " + winningDoor +
            ", pickedDoor = " + pickedDoor +
            ", shownDoor " + shownDoor + ", switchDoor = " + switchDoor);
        if (winningDoor == switchDoor) {
            sb.append(", switching wins");
        }
        return sb.toString();
    }
}
