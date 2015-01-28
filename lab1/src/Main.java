import TSim.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;



public class Main {
    
    public static void main(String[] args) {
        new Main(args);
    }

    public Main(String[] args) {
        
        final List<Sensor> sensors = new ArrayList<>();
        final List<Semaphore> sections = new ArrayList<>();
        final List<Point> switches = new ArrayList<>();
        switches.add(new Point(17, 7));
        switches.add(new Point(15, 9));
        switches.add(new Point(4, 9));
        switches.add(new Point(3, 11));
        
        for (int i = 0; i < 6; i++){
            sections.add(new Semaphore(1, true));
        }
        
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
        
        sensors.add(new StationSensor(13, 11, Train.POS_DIRECTION));
        sensors.add(new StationSensor(13, 13, Train.POS_DIRECTION));
        
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
        
        sensors.add(new StationSensor(14, 3, Train.NEG_DIRECTION));
        sensors.add(new StationSensor(14, 5, Train.NEG_DIRECTION));
        
        
        
        
        

        //TSimInterface.getInstance().setDebug(true);
        final Train firstTrain = new Train(1, 0, 100, Train.POS_DIRECTION, sensors, sections.get(0));
        final Train secondTrain = new Train(2, 15, 100, Train.NEG_DIRECTION, sensors, sections.get(5));
        new Thread(firstTrain).start();
        new Thread(secondTrain).start();
    }
}
