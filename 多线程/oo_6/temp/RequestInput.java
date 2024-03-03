import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class RequestInput extends Thread {
    private final RequestQueue requestQueueHashMap;
    private Maintainer maintainer;

    public RequestInput(RequestQueue requestQueueHashMap) {
        this.requestQueueHashMap = requestQueueHashMap;
        this.maintainer = new Maintainer();
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                synchronized (requestQueueHashMap) {
                    requestQueueHashMap.setOver(Boolean.TRUE);
                    requestQueueHashMap.notifyAll();
                }
                break;
            }
            if (request instanceof MaintainRequest) {
                MaintainRequest temp = (MaintainRequest) request;
                maintainer.getElevators().get(temp.getElevatorId()).setMaintain(true);
                synchronized (requestQueueHashMap) {
                    requestQueueHashMap.notifyAll();
                }
            } else if (request instanceof PersonRequest) {
                PersonRequest temPerson = (PersonRequest)request;
                Person temp = new Person(temPerson.getPersonId(),
                        temPerson.getFromFloor(), temPerson.getToFloor());
                synchronized (requestQueueHashMap) {
                    requestQueueHashMap.addPerson(temp.getSrc(), temp);
                    requestQueueHashMap.notifyAll();
                }
            } else {
                ElevatorRequest temp = (ElevatorRequest) request;
                Elevator elevator = new Elevator(requestQueueHashMap, temp.getElevatorId(),
                        temp.getFloor(), temp.getCapacity(), temp.getSpeed());
                maintainer.getElevators().put(temp.getElevatorId(), elevator);
                elevator.start();
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Maintainer getMaintainer() {
        return maintainer;
    }
}
