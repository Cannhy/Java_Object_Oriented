import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class RequestInput extends Thread {
    private final RequestQueue requestQueueHashMap;
    private Maintainer maintainer;

    public RequestInput(RequestQueue requestQueueHashMap, Maintainer maintainer) {
        this.requestQueueHashMap = requestQueueHashMap;
        this.maintainer = maintainer;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        maintainer.Floyd();
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
                //maintainer.freshMaintain(temp.getElevatorId());
                //maintainer.getElevators().remove(temp.getElevatorId());
            } else if (request instanceof PersonRequest) {
                PersonRequest temPerson = (PersonRequest)request;
                Person temp = new Person(temPerson.getPersonId(),
                        temPerson.getFromFloor(), temPerson.getToFloor());
                maintainer.setRoute(temp);
                temp.setDes();
                /*System.out.println(temp.getSrc() + " " + temp.getDes());
                for (Integer tem1p : temp.getNextDes()) {
                    System.out.println(tem1p);
                }*/
                synchronized (requestQueueHashMap) {
                    requestQueueHashMap.addPerson(temp.getSrc(), temp);
                    requestQueueHashMap.notifyAll();
                }
            } else {
                ElevatorRequest temp = (ElevatorRequest) request;
                Elevator elevator = new Elevator(requestQueueHashMap, temp.getElevatorId(),
                        temp.getFloor(), temp.getCapacity(),
                        temp.getSpeed(), temp.getAccess(), maintainer);
                maintainer.getElevators().put(temp.getElevatorId(), elevator);
                maintainer.freshAdd(temp.getElevatorId());
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
