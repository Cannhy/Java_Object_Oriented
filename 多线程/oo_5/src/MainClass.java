import com.oocourse.elevator1.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueue requestQueue = new RequestQueue();
        RequestInput producer = new RequestInput(requestQueue);
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(requestQueue, i);
            elevator.start();
        }
        producer.start();
    }
}