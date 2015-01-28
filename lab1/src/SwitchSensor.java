
import TSim.*;
import java.awt.Point;
import java.util.concurrent.Semaphore;

public class SwitchSensor extends ClaimSensor {
    private final TSimInterface tsi = TSimInterface.getInstance();
    private final Point trainSwitch;
    private final int switchDir;
    private final boolean alternate;

    public SwitchSensor(int x, int y, Semaphore semaphore, int direction, Point trainSwitch, int switchDir, boolean alternate) {
        super(x, y, semaphore, direction);
        this.trainSwitch = trainSwitch;
        this.switchDir = switchDir;
        this.alternate = alternate;
    }

    @Override
    public void activateSensor(Train train, int status) throws CommandException, InterruptedException {
        if (status == SensorEvent.ACTIVE) {
            final Semaphore semaphore = getSemaphore();
            if (semaphore.availablePermits() > 0) {
                train.claimSemaphore(semaphore);
                tsi.setSwitch((int)trainSwitch.getX(), (int)trainSwitch.getY(), switchDir);
            } else if (alternate){
                final int alternateSwitchDir = (switchDir == TSimInterface.SWITCH_LEFT ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT);
                tsi.setSwitch((int)trainSwitch.getX(), (int)trainSwitch.getY(), alternateSwitchDir);
            } else {
                train.stopTrain();
                train.claimSemaphore(semaphore);
                tsi.setSwitch((int)trainSwitch.getX(), (int)trainSwitch.getY(), switchDir);
                train.startTrain();
            }
        }
    }
}
