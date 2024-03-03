import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class RequestInput extends Thread {
    private final RequestQueue requestQueueHashMap;

    public RequestInput(RequestQueue requestQueueHashMap) {
        this.requestQueueHashMap = requestQueueHashMap;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                synchronized (requestQueueHashMap) {
                    requestQueueHashMap.setOver(Boolean.TRUE);
                    requestQueueHashMap.notifyAll();
                }
                break;
            }
            Person temp = new Person(request.getPersonId(),
                    request.getFromFloor(), request.getToFloor());
            synchronized (requestQueueHashMap) {
                requestQueueHashMap.addPerson(temp.getSrc(), temp);
                requestQueueHashMap.notifyAll();
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
