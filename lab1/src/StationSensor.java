public class StationSensor extends Sensor{
    

    public StationSensor(int x, int y, int direction){
        super(x, y, direction);
    }
    
    @Override
    public void activateSensor(int x, int y, Train train, int status) throws TSim.CommandException, InterruptedException{
        if (matchingSensor(x, y, train.getDirection())){
            train.stopAtStation();
        }
    }

    
}
