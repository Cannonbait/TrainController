
import TSim.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Lab1 {

    public static void main(String[] args) {
        new Lab1(args);
    }

    public Lab1(String[] args) {
        final int firstSpeed, secondSpeed, simulationSpeed;
        final List<Semaphore> sections = new ArrayList<>();

        if (args.length >= 1) {
            firstSpeed = Integer.parseInt(args[0]);
        } else {
            firstSpeed = 1;
        }
        if (args.length >= 2) {
            secondSpeed = Integer.parseInt(args[1]);
        } else {
            secondSpeed = 1;
        }
        if (args.length == 3) {
            simulationSpeed = Integer.parseInt(args[2]);
        } else {
            simulationSpeed = 100;
        }

        for (int i = 0; i < 6; i++) {
            sections.add(new Semaphore(1, true));
        }
        //TSimInterface.getInstance().setDebug(true);
        final Train firstTrain = new Train(1, firstSpeed, simulationSpeed * 2, Train.POS_DIRECTION, createSensors(sections), sections.get(0));
        final Train secondTrain = new Train(2, secondSpeed, simulationSpeed * 2, Train.NEG_DIRECTION, createSensors(sections), sections.get(5));
        new Thread(firstTrain).start();
        new Thread(secondTrain).start();
    }


    //Give physical sensors different behaviours
    //eg sensors.add(new ClaimSensor(6, 6, sections.get(1), Train.POS_DIRECTION)); will add claimsensor-behaviour to physical sensor on [6, 6]
    //[6, 6] will for example also be a releasesensor as of sensors.add(new ReleaseSensor(6, 6, sections.get(1), Train.NEG_DIRECTION));
    
    private List<Sensor> createSensors(List<Semaphore> sections){
        final List<Point> switches = new ArrayList<>();
        final List<Sensor> sensors = new ArrayList<>();
        switches.add(new Point(17, 7));
        switches.add(new Point(15, 9));
        switches.add(new Point(4, 9));
        switches.add(new Point(3, 11));
        
        sensors.add(new ClaimSensor(6, 6, sections.get(1), Train.POS_DIRECTION));
        sensors.add(new ClaimSensor(8, 5, sections.get(1), Train.POS_DIRECTION));

        sensors.add(new ReleaseSensor(19, 9, sections.get(0), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(10, 7, sections.get(1), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(10, 8, sections.get(1), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(12, 9, sections.get(2), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(12, 10, sections.get(2), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(1, 10, sections.get(3), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(6, 11, sections.get(4), Train.POS_DIRECTION));
        sensors.add(new ReleaseSensor(6, 13, sections.get(4), Train.POS_DIRECTION));

        sensors.add(new SwitchSensor(15, 7, sections.get(2), Train.POS_DIRECTION, switches.get(0), TSimInterface.SWITCH_RIGHT, false));
        sensors.add(new SwitchSensor(15, 8, sections.get(2), Train.POS_DIRECTION, switches.get(0), TSimInterface.SWITCH_LEFT, false));
        sensors.add(new SwitchSensor(19, 9, sections.get(3), Train.POS_DIRECTION, switches.get(1), TSimInterface.SWITCH_RIGHT, true));
        sensors.add(new SwitchSensor(7, 9, sections.get(4), Train.POS_DIRECTION, switches.get(2), TSimInterface.SWITCH_LEFT, false));
        sensors.add(new SwitchSensor(7, 10, sections.get(4), Train.POS_DIRECTION, switches.get(2), TSimInterface.SWITCH_RIGHT, false));
        sensors.add(new SwitchSensor(1, 10, sections.get(5), Train.POS_DIRECTION, switches.get(3), TSimInterface.SWITCH_LEFT, true));

        sensors.add(new StationSensor(15, 11, Train.POS_DIRECTION));
        sensors.add(new StationSensor(15, 13, Train.POS_DIRECTION));

        sensors.add(new ClaimSensor(10, 7, sections.get(1), Train.NEG_DIRECTION));
        sensors.add(new ClaimSensor(10, 8, sections.get(1), Train.NEG_DIRECTION));

        sensors.add(new ReleaseSensor(6, 6, sections.get(1), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(8, 5, sections.get(1), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(15, 7, sections.get(2), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(15, 8, sections.get(2), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(19, 9, sections.get(3), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(7, 9, sections.get(4), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(7, 10, sections.get(4), Train.NEG_DIRECTION));
        sensors.add(new ReleaseSensor(1, 10, sections.get(5), Train.NEG_DIRECTION));

        sensors.add(new SwitchSensor(19, 9, sections.get(0), Train.NEG_DIRECTION, switches.get(0), TSimInterface.SWITCH_RIGHT, true));
        sensors.add(new SwitchSensor(12, 9, sections.get(2), Train.NEG_DIRECTION, switches.get(1), TSimInterface.SWITCH_RIGHT, false));
        sensors.add(new SwitchSensor(12, 10, sections.get(2), Train.NEG_DIRECTION, switches.get(1), TSimInterface.SWITCH_LEFT, false));
        sensors.add(new SwitchSensor(1, 10, sections.get(3), Train.NEG_DIRECTION, switches.get(2), TSimInterface.SWITCH_LEFT, true));
        sensors.add(new SwitchSensor(6, 11, sections.get(4), Train.NEG_DIRECTION, switches.get(3), TSimInterface.SWITCH_LEFT, false));
        sensors.add(new SwitchSensor(6, 13, sections.get(4), Train.NEG_DIRECTION, switches.get(3), TSimInterface.SWITCH_RIGHT, false));

        sensors.add(new StationSensor(15, 3, Train.NEG_DIRECTION));
        sensors.add(new StationSensor(15, 5, Train.NEG_DIRECTION));
        return sensors;
    }
}

class Train implements Runnable {
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
        section.tryAcquire();
        claimedSemaphores.add(section);
    }

    public void claimSemaphore(Semaphore semaphore) throws InterruptedException {
        semaphore.acquire();
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
        tsi.setSpeed(id, 0);
    }

    public void startTrain() throws CommandException {
        tsi.setSpeed(id, targetSpeed);
    }

    public void stopAtStation() throws CommandException, InterruptedException {
        stopTrain();
        Thread.sleep(Math.abs(targetSpeed) * delay);
        targetSpeed = (-targetSpeed);
        //Changes POS_DIRECTION to NEG_DIRECTION and NEG_DIRECTION to POS_DIRECTION
        direction = (direction == POS_DIRECTION ? NEG_DIRECTION : POS_DIRECTION);
        startTrain();
    }

    @Override
    public void run() {
        try {
            startTrain();
            while (true) {

                SensorEvent e = tsi.getSensor(id);
                for (Sensor sensor : sensors) {
                    if (sensor.triggerSensor(e.getXpos(), e.getYpos(), e.getStatus(), direction)) {
                        sensor.activateSensor(this);
                    }
                }
            }
        } catch (CommandException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//Represents any sensor with a certain behaviour
//A physical sensor can have several behaviours
//eg a physical sensor on 17, 7 might trigger many behavioural sensors
//such as "ClaimSensor" and "SwitchSensor"
abstract class Sensor {
    private final int x, y, status, direction;

    public Sensor(int x, int y, int status, int direction) {
        this.x = x;
        this.y = y;
        this.status = status;
        this.direction = direction;
    }

    //Returns true if this sensor is at [x, y], triggered by status and direction
    public boolean triggerSensor(int x, int y, int status, int direction) {
        return this.x == x && this.y == y && this.status == status && this.direction == direction;
    }

    //The behaviour of the sensor upon getting triggered
    public abstract void activateSensor(Train train) throws TSim.CommandException, InterruptedException;
}

//A sensor which claims a semaphore on getting triggered
class ClaimSensor extends Sensor {
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

//A sensor which claims a semaphore and sets a switch on getting triggered
class SwitchSensor extends ClaimSensor {
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
    public void activateSensor(Train train) throws CommandException, InterruptedException {
        final Semaphore semaphore = getSemaphore();
        if (semaphore.availablePermits() > 0) {
            train.claimSemaphore(semaphore);
            tsi.setSwitch((int) trainSwitch.getX(), (int) trainSwitch.getY(), switchDir);
        } else if (alternate) {
            final int alternateSwitchDir = (switchDir == TSimInterface.SWITCH_LEFT ? TSimInterface.SWITCH_RIGHT : TSimInterface.SWITCH_LEFT);
            tsi.setSwitch((int) trainSwitch.getX(), (int) trainSwitch.getY(), alternateSwitchDir);
        } else {
            train.stopTrain();
            train.claimSemaphore(semaphore);
            tsi.setSwitch((int) trainSwitch.getX(), (int) trainSwitch.getY(), switchDir);
            train.startTrain();
        }
    }
}

//A sensor that releases a semaphore on getting triggered
class ReleaseSensor extends Sensor {
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

//A sensor which stops a train on getting triggered
class StationSensor extends Sensor {
    public StationSensor(int x, int y, int direction) {
        super(x, y, SensorEvent.ACTIVE, direction);
    }

    @Override
    public void activateSensor(Train train) throws TSim.CommandException, InterruptedException {
        train.stopAtStation();
    }
}
