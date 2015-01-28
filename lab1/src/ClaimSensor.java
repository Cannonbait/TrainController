
import java.util.concurrent.Semaphore;
import TSim.*;

public class ClaimSensor extends Sensor {

    private final TSimInterface tsi = TSimInterface.getInstance();
    private final Semaphore semaphore;

    public ClaimSensor(int x, int y, Semaphore semaphore, int direction) {
        super(x, y, SensorEvent.ACTIVE, direction);
        this.semaphore = semaphore;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    @Override
    public void activateSensor(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        train.claimSemaphore(semaphore);
        train.startTrain();
    }
}
