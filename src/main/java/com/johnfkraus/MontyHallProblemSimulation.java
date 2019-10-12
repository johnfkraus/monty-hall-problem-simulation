package com.johnfkraus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

enum Door {
    ONE, TWO, THREE
}

class MontyHallProblemSimulation {
    // all game objects will be stored in an ArrayList for printing results
    List<Game> games = new ArrayList<>();
    int stayWinsX = 0;
    int switchWinsX = 0;

    void run() throws Exception {
        int runs = 1000;
        for (int i = 0; i < runs; i++) {
            Game game = new Game();
            if (game.originalChoiceWinsX) {
                stayWinsX++;
            }
            if (game.switchWinsX) {
                switchWinsX++;
            }
            games.add(game); // save for possible further analysis
            System.out.print("Game #" + (i + 1) + ", ");
            System.out.println(game.toString());
        }
        // final results of all games
        System.out.println("stayWinsX = " + stayWinsX + ", switchWinsX = " + switchWinsX + ", #games = " + games.size());
    }

    public static void main(String... args) throws Exception {
        MontyHallProblemSimulation mh = new MontyHallProblemSimulation();
        mh.run();
    }
}

class Game {
    static Door[] doorArr = {Door.ONE, Door.TWO, Door.THREE}; // there are three doors from which to choose.
    Door[] doorsMontyCanShow = doorArr.clone();
    List<Door> doorListX = new ArrayList<Door>(Arrays.asList(doorArr)); // {Door.ONE, Door.TWO, Door.THREE}));
    Door winningDoorX = null; // the winning door, chosen at random; there is a new car behind this door; behind the other two doors are goats. Is it a little sad that no one ever wanted one of the goats?  If you had won a goat in on Let's Make a Deal in the 1960s you might still have a nice little herd of goat.  But if you won a 1960s-era car, if it didn't kill you it would for sure now be worthless and rusting away in some dump.  What ever happened to those goats, anyway?
    Door pickedDoorX = null;

    Door shownDoorX = null; // After the contestant chooses a door, Monty (always, we assume, perhaps unrealistically) opens a door behind which is a goat.  If the contestant's first choice was the door with the car, Monty will choose one of the two goat doors at random.
    Door switchDoorX; // the remaining door to which the contestant can opt to switch.
    boolean originalChoiceWinsX;
    boolean switchWinsX;

    // the winning and picked doors are randomly selected; they can be the same door or different doors
    Game() throws Exception {
        winningDoorX = pickRandomDoorX();
        pickedDoorX = pickRandomDoorX();
        doorListX.remove(winningDoorX);
        doorListX.remove(pickedDoorX);
        shownDoorX = pickShownDoorX(doorListX); // After contestant pick a door Monty always opens a goat door and lets contestant choose to switch;
        testResults(); // make sure Monty didn't make a mistake
        if (winningDoorX == pickedDoorX) {
            originalChoiceWinsX = true;
        }
        switchDoorX = pickSwitchDoorX();
        if (winningDoorX == switchDoorX) {
            switchWinsX = true;
        }
    }

    void testResults() throws Exception {
        // check for disallowed conditions
        // Monty will not show the winning door
        if (shownDoorX == winningDoorX) {
            throw new Exception("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors.");
        }
        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        if (shownDoorX == pickedDoorX) {
            throw new Exception("Monty must not show the door picked by the contestant until contestant has a chance to switch doors.");
        }
    }

    static Door pickRandomDoorX() {
        Random r = new Random();
        int randomNumber = r.nextInt(doorArr.length);
        Door pickedDoor = doorArr[randomNumber];
        // System.out.println("pickedRandomDoorX() = " + pickedDoor);
        //return doorArr[randomNumber];
        return pickedDoor;
    }


/*
    static int pickShownDoor(List<Integer> doorList) {
        Integer[] arr = doorList.toArray(new Integer[0]);
        Random r = new Random();
        int randomNumber = r.nextInt(arr.length);
        return arr[randomNumber];
    }
*/
    // pick the goat door that Monty will open before offering to let contestant switch doors;
    // pick the (goat) door that Monty will open before offering to let contestant switch doors; If Monty can choose from two goat doors, he chooses one of the two randomly
    static Door pickShownDoorX(List<Door> doorList) {
        Random r = new Random();
        int randomNumber = r.nextInt(doorList.size());
        return doorList.get(randomNumber);
    }

    // To which door can the contestant switch?  It's the door left after you take away the contestant's first choice door and the goat door Monty opened.
    Door pickSwitchDoorX() throws Exception {
        Set<Door> allDoors = new HashSet<>();
        allDoors.add(Door.ONE);
        allDoors.add(Door.TWO);
        allDoors.add(Door.THREE);
        Set<Door> nonSwitchDoors = new HashSet<Door>();
        nonSwitchDoors.add(pickedDoorX); // Contestant can't switch to the door already chosen.
        nonSwitchDoors.add(shownDoorX); // Contestant can't/won't switch to the goat door Monty has opened
        allDoors.removeAll(nonSwitchDoors);
        // there should be only one door left in the allDoors Set.
        if (allDoors.size() != 1) {
            throw new Exception("There should be only one door available to which contestant can switch.");
        }
        return allDoors.iterator().next();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("winningDoorX = " + winningDoorX +
            ", pickedDoorX = " + pickedDoorX +
            ", shownDoorX " + shownDoorX + ", switchDoorX = " + switchDoorX);
        if (winningDoorX == switchDoorX) {
            sb.append(", switching wins");
        }
        return sb.toString();
    }

}

