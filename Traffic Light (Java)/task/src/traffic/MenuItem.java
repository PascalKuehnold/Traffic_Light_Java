package traffic;

public enum MenuItem {
    ADD("1", "Add"),
    DELETE("2", "Delete"),
    SYSTEM("3", "System"),
    QUIT("0", "Quit");


    private final String value;
    private final String operation;

    MenuItem(String number, String operation) {
        this.value = number;
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }
}
