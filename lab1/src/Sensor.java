
public abstract class Sensor {
    private final int x, y, status, direction;
    public Sensor(int x, int y, int status, int direction){
        this.x = x;
        this.y = y;
        this.status = status;
        this.direction = direction;
    }
    
    public boolean matchingSensor(int x, int y, int status, int direction){
        return this.x==x && this.y == y && this.status == status && this.direction == direction;
    }
    
    public abstract void activateSensor(Train train) throws TSim.CommandException, InterruptedException;
}
