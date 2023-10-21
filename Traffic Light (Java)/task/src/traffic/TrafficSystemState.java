package traffic;

public enum TrafficSystemState {
    NOT_STARTED("Not Started"),
    MENU("Menu"),
    SYSTEM("System");

    private final String stateName;

    TrafficSystemState(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
