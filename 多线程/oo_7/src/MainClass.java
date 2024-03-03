import com.oocourse.elevator3.TimableOutput;

public class MainClass {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Maintainer maintainer = new Maintainer();
        RequestQueue requestQueue = new RequestQueue();
        RequestInput producer = new RequestInput(requestQueue, maintainer);
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(requestQueue, i, 1, 6, 0.4, 2047, maintainer);
            elevator.start();
            producer.getMaintainer().putEle(i, elevator);
        }
        producer.start();
        for (int i = 0; i <= 11; i++) {
            Object obj = new Object();
            DoorLock.getLocks().add(obj);
        } //虚空锁
    }
}
