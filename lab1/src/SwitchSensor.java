
import TSim.*;
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
    public void activateSensor(int x, int y, Train train, int status) throws CommandException, InterruptedException {
        if (matchingSensor(x, y, train.getDirection()) && status == SensorEvent.ACTIVE) {
            final Semaphore semaphore = getSemaphore();
            if (semaphore.tryAcquire()) {
                train.claimSemaphore(semaphore);
                tsi.setSwitch(trainSwitch.getX(), trainSwitch.getY(), switchDir);
            } else if (alternate){
                final int alternateSwitchDir = (switchDir == TSimInterface.SWITCH_LEFT ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT);
                tsi.setSwitch(trainSwitch.getX(), trainSwitch.getY(), alternateSwitchDir);
            } else {
                train.stopTrain();
                semaphore.acquire();
                train.claimSemaphore(semaphore);
                tsi.setSwitch(trainSwitch.getX(), trainSwitch.getY(), switchDir);
                train.startTrain();
            }
        }
    }
}
