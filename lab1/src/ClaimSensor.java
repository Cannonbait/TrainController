
import java.util.concurrent.Semaphore;
import TSim.*;

public class ClaimSensor extends Sensor {

    private final TSimInterface tsi = TSimInterface.getInstance();
    private final Semaphore semaphore;

    public ClaimSensor(int x, int y, Semaphore semaphore, int direction) {
        super(x, y, direction);
        this.semaphore = semaphore;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    @Override
    public void activateSensor(int x, int y, Train train, int status) throws CommandException, InterruptedException {
        if (matchingSensor(x, y, train.getDirection())) {
            if (status == SensorEvent.ACTIVE) {
                train.stopTrain();
                train.claimSemaphore(semaphore);
                train.startTrain();

            }
        }
    }
}
