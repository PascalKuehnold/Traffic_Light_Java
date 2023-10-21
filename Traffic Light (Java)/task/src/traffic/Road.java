package traffic;

public class Road {
    private String roadName;
    private boolean isOpen;
    private String roadStatus;
    private int closedInterval;
    private int openInterval;

    private int roadInterval;

    public Road(String roadName) {
        this.roadName = roadName;
        setRoadStatus();
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getClosedInterval() {
        return closedInterval;
    }

    public void setClosedInterval(int closedInterval) {
        this.closedInterval = closedInterval;
    }

    public int getOpenInterval() {
        return openInterval;
    }

    public void setOpenInterval(int openInterval) {
        this.openInterval = openInterval;
    }


    public int getRoadInterval() {
        return roadInterval;
    }

    public void setRoadInterval(int roadInterval) {
        this.roadInterval = roadInterval;
    }

    public String getRoadStatus() {
        return this.roadStatus;
    }

    private void setRoadStatus(){
        if(this.isOpen){
                this.roadStatus = this.roadName + " will be " + "\u001B[32m" + "open for "  + this.roadInterval + "s." +"\u001B[0m";
        } else {
            this.roadStatus = this.roadName  + " will be " +  "\u001B[31m" + "closed for " + this.roadInterval + "s." + "\u001B[0m" ;
        }
    }

    public void printRoadStatus(){
        setRoadStatus();
        System.out.println(this.roadStatus);
    }

}
