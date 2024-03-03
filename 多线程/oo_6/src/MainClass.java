import com.oocourse.elevator2.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueue requestQueue = new RequestQueue();
        RequestInput producer = new RequestInput(requestQueue);
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(requestQueue, i, 1, 6, 0.4);
            elevator.start();
            producer.getMaintainer().putEle(i, elevator);
        }
        producer.start();
    }
}