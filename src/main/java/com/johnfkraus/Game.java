package com.johnfkraus;

import java.util.*;

public class Game {
    private int gameNumber;
    private static Door[] doorArr = {Door.ONE, Door.TWO, Door.THREE}; // there are three doors from which to choose.
    private List<Door> doorList = new ArrayList<>(Arrays.asList(doorArr)); // {Door.ONE, Door.TWO, Door.THREE}));
    Door winningDoor; // the winning door, chosen at random; there is a new car behind this door; behind the other two doors are goats. Is it a little sad that no one ever wanted one of the goats?  If you had won a goat in on Let's Make a Deal in the 1960s you might still have a nice little herd of goats.  But if you won a 1960s-era car, if it didn't kill you it would for sure now be worthless and rusting away in some dump.  What ever happened to those goats, anyway?
    Door pickedDoor;
    Door shownDoor; // After the contestant chooses a door, Monty (always, we assume, perhaps unrealistically) opens a door behind which is a goat.  If the contestant's first choice was the door with the car, Monty will choose one of the two goat doors at random.  If the contestant's first choice was a goat door, Monty will show the other goat door.
    Door switchDoor; // the remaining door to which the contestant can opt to switch.
    boolean originalChoiceWins;
    boolean switchWins;
    // the winning and picked doors are randomly selected; they can be the same door or different doors
    public Game() {}
    Game(int gameNumber) {
        this.gameNumber = gameNumber;
        winningDoor = pickRandomDoor();
        pickedDoor = pickRandomDoor();
        doorList.remove(winningDoor);
        doorList.remove(pickedDoor);
        shownDoor = pickShownDoor(doorList); // After contestant picks a door Monty is assumed to always open a goat door and give the contestant the opportunity to switch to the other unopened door.  In reality, Monty did not always give the contestant the chance to make this switch.
        switchDoor = pickSwitchDoor();

        if (winningDoor == pickedDoor) {
            originalChoiceWins = true;
        } else if (winningDoor == switchDoor) {
            switchWins = true;
        } else {
            throw new RuntimeException("Program logic error, apparently.");
        }
        repInvariant(); // make sure Monty or the contestant didn't make a mistake
    }

    private void repInvariant() {
        // check for disallowed conditions
        if (originalChoiceWins == switchWins) {
            throw new RuntimeException("The contestant can win by sticking with their original choice or by switching but not both.");
        }
        // When offering the contestant the chance to switch doors, Monty must not show the winning door
        if (shownDoor == winningDoor) {
            throw new RuntimeException("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors.");
        }
        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        if (shownDoor == pickedDoor) {
            throw new RuntimeException("Monty must not show the door initially picked by the contestant until contestant has a chance to switch doors.");
        }
        if (shownDoor == switchDoor) {
            throw new RuntimeException("Contestant would not logically choose the opened door behind which a goat has already been revealed.");
        }
    }

    private static Door pickRandomDoor() {
        Random r = new Random();
        int randomNumber = r.nextInt(doorArr.length);
        return doorArr[randomNumber];
    }

    // pick the goat door that Monty will open before offering to let contestant switch doors;
    // pick the (goat) door that Monty will open before offering to let contestant switch doors; If Monty can choose from two goat doors, he chooses one of the two randomly
    private static Door pickShownDoor(List<Door> doorList) {
        Random r = new Random();
        int randomNumber = r.nextInt(doorList.size());
        return doorList.get(randomNumber);
    }

    // To which door can the contestant switch?  It's the door left after you take away the contestant's first choice door and the goat door Monty opened.
    private Door pickSwitchDoor() {
        Set<Door> allDoors = new HashSet<>();
        allDoors.add(Door.ONE);
        allDoors.add(Door.TWO);
        allDoors.add(Door.THREE);
        Set<Door> nonSwitchDoors = new HashSet<>();
        nonSwitchDoors.add(pickedDoor); // Contestant can't switch to the door already chosen.
        nonSwitchDoors.add(shownDoor); // Contestant can't/won't switch to the goat door Monty has opened
        allDoors.removeAll(nonSwitchDoors);
        // there should be only one door left in the allDoors Set.
        if (allDoors.size() != 1) {
            throw new RuntimeException("There should be only one door available to which contestant can switch.");
        }
        return allDoors.iterator().next();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game #").append(gameNumber).append(", winningDoor = ").append(winningDoor).append(", pickedDoor = ").append(pickedDoor).append(", shownDoor ").append(shownDoor).append(", switchDoor = ").append(switchDoor)
            .append(winningDoor == switchDoor ? ", switching wins" : "");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameNumber == game.gameNumber &&
            originalChoiceWins == game.originalChoiceWins &&
            switchWins == game.switchWins &&
            doorList.equals(game.doorList) &&
            winningDoor == game.winningDoor &&
            pickedDoor == game.pickedDoor &&
            shownDoor == game.shownDoor &&
            switchDoor == game.switchDoor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameNumber, doorList, winningDoor, pickedDoor, shownDoor, switchDoor, originalChoiceWins, switchWins);
    }
}
