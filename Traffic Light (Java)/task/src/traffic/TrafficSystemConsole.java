package traffic;

import java.util.*;

public class TrafficSystemConsole {
    private int roads;
    private int interval;

    private Deque<Road> addedRoads;

    private static volatile TrafficSystemState currentState = TrafficSystemState.NOT_STARTED;
    private TrafficSystemStatus trafficSystemThread;
    private volatile boolean isTrafficSystemThreadRunning = true;


    Scanner scanner;

    public TrafficSystemConsole() {
        printWelcomeMessage();

        scanner = new Scanner(System.in);

        readBaseInput();

        createSystemThread();

        scanner.nextLine();

        mainLoop();

        scanner.close();
    }


    private void readBaseInput() {
        readRoadsInput();
        readIntervalInput();

        setState(TrafficSystemState.MENU);
    }

    private void createSystemThread() {
        trafficSystemThread = new TrafficSystemStatus(roads, interval, addedRoads);
        trafficSystemThread.setName("QueueThread");
        trafficSystemThread.start();
    }

    private void readRoadsInput() {
        boolean validInput = false;
        System.out.print("Input the number of roads: ");
        while (!validInput) {
            try {
                this.roads = scanner.nextInt();

                if (this.roads > 0) {
                    validInput = true; // The roads input is a positive integer, exit the loop
                    addedRoads = new ArrayDeque<>(roads);
                } else {
                    System.out.print("Incorrect input. Please Try again: ");
                }
            } catch (Exception e) {
                System.out.print("Incorrect input. Please Try again: ");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private void readIntervalInput() {
        boolean validInput = false;
        System.out.print("Input the interval: ");
        while (!validInput) {
            try {
                this.interval = scanner.nextInt();

                if (this.interval > 0) {
                    validInput = true; // The interval input is a positive integer, exit the loop
                } else {
                    System.out.print("Incorrect input. Please Try again: ");
                }
            } catch (Exception e) {
                System.out.print("Incorrect input. Please Try again: ");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }


    private void addRoad() {
        System.out.print("Input road name: ");
        String roadName = scanner.nextLine().trim(); // Read and trim the input

        if (addedRoads.size() == this.roads) {
            System.out.println("Queue is full");
            return;
        }

        if (roadName.isEmpty()) {
            System.out.println("The road must have a name.");
        } else {
            //Adds an element to the Queue
            Road tempRoad = new Road(roadName);
            addedRoads.offer(tempRoad);
            if (addedRoads.size() == 1) {
                setFirstLights(tempRoad);
                addedRoads.getFirst().setOpen(true);
            } else if (addedRoads.size() == 2) {
                Road firstRoad = addedRoads.getFirst();
                firstRoad.setOpen(true);
                firstRoad.setClosedInterval(interval);
                tempRoad.setOpenInterval(interval);
                tempRoad.setRoadInterval(firstRoad.getRoadInterval());
                tempRoad.setClosedInterval(interval * (addedRoads.size() - 1));
            } else {
                setLightsIfMoreThanTwo(tempRoad);
            }


            System.out.println(roadName + " added!");
        }
    }

    private void setLightsIfMoreThanTwo(Road tempRoad) {
        tempRoad.setOpenInterval(interval);
        tempRoad.setClosedInterval(interval * (addedRoads.size() - 1));
        tempRoad.setRoadInterval(getHighestRoadInterval());
    }

    private int getHighestRoadInterval() {
        Optional<Road> maxClosedRoad = addedRoads.stream()
                .filter(road -> !road.isOpen())
                .max(Comparator.comparingInt(Road::getRoadInterval));

        return maxClosedRoad.map(road -> road.getRoadInterval() + interval).orElseGet(() -> interval * (addedRoads.size() - 1));

    }

    private void setFirstLights(Road tempRoad) {
        tempRoad.setOpenInterval(interval);
        tempRoad.setClosedInterval(interval);
        tempRoad.setRoadInterval(interval);
    }

    private void deleteRoad() {
        refreshRoadTimers();


        try {
            Road roadToDelete = addedRoads.peek();

            if (roadToDelete == null) {
                System.out.println("Queue is empty.");
                return;
            }
            //Removes the first element in the Queue
            addedRoads.poll();

            System.out.print(roadToDelete.getRoadName() + " deleted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshRoadTimers() {
        addedRoads.forEach(road -> {
            road.setClosedInterval(road.getClosedInterval() - interval);
        });
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to the traffic management system!");
    }

    private void showMenu() {
        System.out.println("Menu:");
        for (MenuItem menuItem : MenuItem.values()) {
            System.out.printf("%s. %s\n", menuItem.getValue(), menuItem.getOperation());
        }
    }

    private void mainLoop() {
        while (true) {
            TrafficSystemUtils.clearConsole();
            showMenu();
            String userInput = scanner.nextLine();

            if (userInput.equals(MenuItem.ADD.getValue())) {
                addRoad();
            } else if (userInput.equals(MenuItem.DELETE.getValue())) {
                deleteRoad();
            } else if (userInput.equals(MenuItem.SYSTEM.getValue())) {
                System.out.println("System opened");
                setState(TrafficSystemState.SYSTEM);
            } else if (userInput.equals(MenuItem.QUIT.getValue())) {
                trafficSystemThread.stopThread();
                System.out.println("Bye!");
                break; // Exit the loop when the user selects "Quit"
            } else {
                System.out.println("Incorrect option");
            }

            // Wait for user to press Enter before showing the menu again
            //System.out.println("Press Enter to continue...");
            scanner.nextLine();
            setState(TrafficSystemState.MENU);
        }
        stopTrafficSystemThread();
        scanner.close();
    }

    public void stopTrafficSystemThread() {
        isTrafficSystemThreadRunning = false;
    }

    public void setState(TrafficSystemState newState) {
        currentState = newState;
    }

    public static TrafficSystemState getState() {
        return currentState;
    }
}
