public class StationSensor extends Sensor{
    

    public StationSensor(int x, int y, int direction){
        super(x, y, direction);
    }
    
    @Override
    public void activateSensor(Train train, int status) throws TSim.CommandException, InterruptedException{ 
    	train.stopAtStation();
    }

    
}
