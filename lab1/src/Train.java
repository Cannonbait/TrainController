
import TSim.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Train implements Runnable {

    public static final int POS_DIRECTION = 1, NEG_DIRECTION = 0;

    private final TSimInterface tsi = TSimInterface.getInstance();
    private final int id, delay;
    private int targetSpeed, direction;
    private final List<Sensor> sensors;
    private final List<Semaphore> claimedSemaphores = new ArrayList<>();
    

    public Train(int id, int speed, int delay, int direction, List<Sensor> sensors, Semaphore section) {
        this.id = id;
        targetSpeed = speed;
        this.delay = delay;
        this.direction = direction;
        this.sensors = sensors;
        try {
            tsi.setSpeed(id, speed);
        } catch (CommandException e) {
            e.printStackTrace();
            System.exit(1);
        }
        section.tryAcquire();
        claimedSemaphores.add(section);
    }

    public void claimSemaphore(Semaphore semaphore) {
        claimedSemaphores.add(semaphore);
    }

    public void releaseSemaphore(Semaphore semaphore) {
        if (claimedSemaphores.contains(semaphore)) {
            semaphore.release();
            claimedSemaphores.remove(semaphore);
        }
    }

    public int getDirection() {
        return direction;
    }

    public void stopTrain() throws CommandException {
        setSpeed(0);
    }

    public void startTrain() throws CommandException {
        setSpeed(targetSpeed);
    }

    private void setSpeed(int speed) throws CommandException {
        tsi.setSpeed(id, speed);
    }

    public void stopAtStation() throws CommandException, InterruptedException {
        stopTrain();
        Thread.sleep(Math.abs(targetSpeed) * delay);
        targetSpeed = (-targetSpeed);
        //Changes POS_DIRECTION to NEG_DIRECTION and NEG_DIRECTION to POS_DIRECTION
        direction  = (direction == POS_DIRECTION ? NEG_DIRECTION : POS_DIRECTION);
        startTrain();
    }

    @Override
    public void run() {
        while (true) {
            try {
                SensorEvent e = tsi.getSensor(id);
                for (Sensor sensor : sensors) {
                    sensor.activateSensor(e.getXpos(), e.getYpos(), this, e.getStatus());
                }

            } catch (CommandException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
