package traffic;

import java.util.Comparator;
import java.util.Deque;
import java.util.Optional;

public class TrafficSystemStatus extends Thread {
    private volatile boolean isTrafficSystemThreadRunning = true;
    private int timeSinceStartup;
    private final Deque<Road> addedRoads;
    private final int roads;
    private final int interval;
    int highestClosedInterval;

    public TrafficSystemStatus(int roads, int interval, Deque<Road> addedRoads) {
        this.roads = roads;
        this.interval = interval;
        this.addedRoads = addedRoads;
    }

    @Override
    public void run() {
        while (isTrafficSystemThreadRunning) {
            try {
                TrafficSystemState systemState = TrafficSystemConsole.getState(); // Get the current state
                // Increment the timeSinceStartup by one
                timeSinceStartup++;

                if (systemState == TrafficSystemState.SYSTEM) {
                    TrafficSystemUtils.clearConsole();

                    System.out.printf("! %ds. have passed since system startup !\n", timeSinceStartup);

                    System.out.printf("! Number of roads: %d !\n", roads);
                    System.out.printf("! Interval: %d !\n", interval);

                    Optional<Road> roadWithHighestInterval = addedRoads.stream()
                            .max(Comparator.comparingInt(Road::getClosedInterval));

                    if (roadWithHighestInterval.isPresent()) {
                        Road highestIntervalRoad = roadWithHighestInterval.get();
                        highestClosedInterval = highestIntervalRoad.getClosedInterval();
                    }

                    if (!addedRoads.isEmpty()) {
                        System.out.println();

                        addedRoads.forEach(Road::printRoadStatus);

                        if (addedRoads.size() == 1) {

                            Road activeRoad = addedRoads.getFirst();

                            activeRoad.setRoadInterval(activeRoad.getRoadInterval() - 1);
                            activeRoad.setOpenInterval(interval);
                            activeRoad.setClosedInterval(interval);

                            if (!activeRoad.isOpen()) {
                                activeRoad.setOpen(true);
                            }

                            if (activeRoad.getRoadInterval() == 0) {
                                activeRoad.setRoadInterval(interval);
                            }


                        } else {
                            addedRoads.forEach(
                                    road -> {
                                        road.setRoadInterval(road.getRoadInterval() - 1);

                                        if (road.isOpen() && road.getRoadInterval() == 0) {
                                            road.setOpen(false);
                                            road.setRoadInterval(highestClosedInterval);
                                        }

                                        if (!road.isOpen() && road.getRoadInterval() == 0) {
                                            road.setOpen(true);
                                            road.setRoadInterval(interval);
                                        }

                                    }

                            );
                        }
                        System.out.println();
                    }
                    System.out.println("! Press \"Enter\" to open menu !");
                }
                // Sleep for one second (1000 milliseconds)
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        isTrafficSystemThreadRunning = false;
    }

}