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
    // all game objects will be stored in an ArrayList
    List<Game> games = new ArrayList<>();
    int stayWins = 0;
    int switchWins = 0;
    int stayWinsX = 0;
    int switchWinsX = 0;
    void run() throws Exception {
        int runs = 1000;
        for (int i = 0; i < runs; i++) {
            Game game = new Game();
            if (game.originalChoiceWins) { stayWins++; }
            if (game.switchWins) { switchWins++; }
            if (game.originalChoiceWinsX) { stayWinsX++; }
            if (game.switchWinsX) { switchWinsX++; }
            games.add(game); // save for possible further analysis
            System.out.print("Game #" + (i + 1) + ", ");
            System.out.println(game.toString());
            System.out.print("Game #" + (i + 1) + ", ");
            System.out.println(game.toStringX());
        }
        System.out.println("stayWins = " + stayWins + ", switchWins = " + switchWins + ", #games = " + games.size());
        System.out.println("stayWinsX = " + stayWinsX + ", switchWinsX = " + switchWinsX + ", #games = " + games.size());
    }

    public static void main(String... args) throws Exception {
        MontyHallProblemSimulation mh = new MontyHallProblemSimulation();
        mh.run();
    }
}
class Game {
    Integer[] intArr = {1, 2, 3}; // there are three doors from which to choose.

    List<Integer> doorList = new ArrayList<Integer>(Arrays.asList(intArr));
    static Door[] doorArr = {Door.ONE, Door.TWO, Door.THREE}; // there are three doors from which to choose.
    Door[] doorsMontyCanShow = doorArr.clone();
    List<Door> doorListX = new ArrayList<Door>(Arrays.asList(doorArr)); // {Door.ONE, Door.TWO, Door.THREE}));
    Door winningDoorX = null;
    Integer winningDoor = 0;  // the winning door, chosen at random; there is a new car behind this door; behind the other two doors are goats. Is it a little sad that no one ever wanted one of the goats?  If you had won a goat in on Let's Make a Deal in the 1960s you might still have a nice little herd of goat.  But if you won a 1960s-era car, if it didn't kill you it would for sure now be worthless and rusting away in some dump.  What ever happened to those goats, anyway?
    Integer pickedDoor = 0;  // The door initially picked by the contestant, chosen at random in this model.
    Door pickedDoorX = null;
    Integer shownDoor = 0;  // After the contestant chooses a door, Monty (always, we assume, perhaps unrealistically) opens a door behind which is a goat.  If the contestant's first choice was the door with the car, Monty will choose one of the two goat doors at random.
    Door shownDoorX = null;
    Integer switchDoor = 0; // the remaining door to which the contestant can opt to switch.
    Door switchDoorX;
    boolean originalChoiceWins;
    boolean switchWins;
    boolean originalChoiceWinsX;
    boolean switchWinsX;
    // the winning and picked doors are randomly selected; they can be the same door or different doors
    Game() throws Exception {
        winningDoorX = pickRandomDoorX();
        pickedDoorX = pickRandomDoorX();
        winningDoor = pickRandomDoor();
        pickedDoor = pickRandomDoor(); // contestant randomly picks a door
        doorList.remove(winningDoor);
        doorListX.remove(winningDoorX);
        doorList.remove(pickedDoor);
        doorListX.remove(pickedDoorX);
        shownDoor = pickShownDoor(doorList); // After contestant pick a door Monty always opens a goat door and lets contestant choose to switch;
        shownDoorX = pickShownDoorX(doorListX); // After contestant pick a door Monty always opens a goat door and lets contestant choose to switch;
        testResults(); // make sure Monty didn't make a mistake
        if (winningDoor == pickedDoor) { originalChoiceWins = true; }
        if (winningDoorX == pickedDoorX) { originalChoiceWinsX = true; }
        switchDoor = pickSwitchDoor();
        switchDoorX = pickSwitchDoorX();
        if (winningDoor == switchDoor) { switchWins = true; }
        if (winningDoorX == switchDoorX) { switchWinsX = true; }
    }

    void testResults() throws Exception {
        // check for disallowed conditions
        // Monty will not show the winning door
        if (shownDoor == winningDoor) { throw new Exception("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors."); }
        if (shownDoorX == winningDoorX) { throw new Exception("When inviting the contestant to switch doors, Monty must not show the winning door before contestant has chance to switch doors."); }
        // When inviting contestant to switch doors, Monty will not open the door initially picked by the contestant
        if (shownDoor == pickedDoor) { throw new Exception("Monty must not show the door picked by the contestant until contestant has a chance to switch doors."); }
        if (shownDoorX == pickedDoorX) { throw new Exception("Monty must not show the door picked by the contestant until contestant has a chance to switch doors."); }
    }

    static int pickRandomDoor() {
        int[] arr={1, 2, 3};
        Random r=new Random();
        int randomNumber=r.nextInt(arr.length);
        return arr[randomNumber];
    }
    static Door pickRandomDoorX() {
        // int[] arr={1, 2, 3};
        Random r=new Random();
        int randomNumber=r.nextInt(doorArr.length);
        // return arr[randomNumber];
        Door pickedDoor = doorArr[randomNumber];
        System.out.println("pickedRandomDoorX() = " + pickedDoor);
        //return doorArr[randomNumber];
        return pickedDoor;
    }

    // pick the goat door that Monty will open before offering to let contestant switch doors;

    // pick the (goat) door that Monty will open before offering to let contestant switch doors; If Monty can choose from two goat doors, he chooses one of the two randomly
    static int pickShownDoor(List<Integer> doorList) {
        Integer[] arr = doorList.toArray(new Integer[0]);
        Random r=new Random();
        int randomNumber=r.nextInt(arr.length);
        return arr[randomNumber];
    }
    static Door pickShownDoorX(List<Door> doorList2) {
        // Integer[] arr = doorList.toArray(new Integer[0]);
        Random r=new Random();
        int randomNumber=r.nextInt(doorList2.size());
        return doorList2.get(randomNumber);
    }
    // To which door can the contestant switch?  It's the door left after you take away the contestant's first choice door and the goat door Monty opened.
    int pickSwitchDoor() throws Exception {
        Set<Integer> allDoors = new HashSet<>();
        allDoors.add(1);
        allDoors.add(2);
        allDoors.add(3);
        Set<Integer> nonSwitchDoors = new HashSet<Integer>();
        nonSwitchDoors.add(pickedDoor); // Contestant can't switch to the door already chosen.
        nonSwitchDoors.add(shownDoor); // Contestant can't/won't switch to the goat door Monty has opened
        allDoors.removeAll(nonSwitchDoors);
        if (allDoors.size() != 1) { throw new Exception("There should be only one door available to which contestant can switch."); }
        return allDoors.iterator().next();
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
        if (allDoors.size() != 1) { throw new Exception("There should be only one door available to which contestant can switch."); }
        return allDoors.iterator().next();
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("winningDoor = " + winningDoor +
            ", pickedDoor = " + pickedDoor +
            ", shownDoor " + shownDoor + ", switchDoor = " + switchDoor);
        if (winningDoor == switchDoor) { sb.append(", switching wins"); }
        return sb.toString();
    }

    public String toStringX() {
        StringBuffer sb = new StringBuffer();
        sb.append("winningDoorX = " + winningDoorX +
            ", pickedDoorX = " + pickedDoorX +
            ", shownDoorX " + shownDoorX + ", switchDoorX = " + switchDoorX);
        if (winningDoorX == switchDoorX) { sb.append(", switching wins"); }
        return sb.toString();
    }

}

