
import java.util.concurrent.Semaphore;
import TSim.*;

public class ReleaseSensor extends Sensor {

    private final Semaphore semaphore;

    public ReleaseSensor(int x, int y, Semaphore semaphore, int direction) {
        super(x, y, SensorEvent.INACTIVE, direction);
        this.semaphore = semaphore;
    }

    @Override
    public void activateSensor(Train train) {
        train.releaseSemaphore(semaphore);
    }
}
