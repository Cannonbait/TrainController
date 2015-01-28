
public abstract class Sensor {
    private final int x, y, direction;
    public Sensor(int x, int y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    
    public boolean matchingSensor(int x, int y, int direction){
        return this.x==x && this.y == y && this.direction == direction;
    }
    
    public abstract void activateSensor(Train train, int status) throws TSim.CommandException, InterruptedException;
}
