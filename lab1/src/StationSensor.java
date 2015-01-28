
import TSim.*;

public class StationSensor extends Sensor {

    public StationSensor(int x, int y, int direction) {
        super(x, y, SensorEvent.ACTIVE, direction);
    }

    @Override
    public void activateSensor(Train train) throws TSim.CommandException, InterruptedException {
        train.stopAtStation();
    }

}
