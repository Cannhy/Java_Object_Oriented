import java.util.ArrayList;

public class Scheduler {
    private final RequestQueue requestQueueHashMap;

    public Scheduler(RequestQueue requestQueueHashMap) {
        this.requestQueueHashMap = requestQueueHashMap;
    }

    public void look(Elevator elevator) {
        if (elevator.getMainRequest() == null) {
            synchronized (requestQueueHashMap) {
                if (requestQueueHashMap.isNull()) {
                    return;
                }
                elevator.setMainRequest(this.setMainReq(elevator));
                if (elevator.getMainRequest() == null) {
                    return;
                }
                //System.out.println("in");
                elevator.setDirection(this.nextDirect(elevator));
            }
            //System.out.println("in");
            elevator.arriveTo(elevator.getMainRequest().getSrc());
            if (elevator.isMaintain()) {
                return;
            }
            if (!elevator.arriveAble(elevator.getMainRequest().getDes())) {
                elevator.getMainRequest().setMain(false);
                return;
            }
            elevator.printEle(1);
            elevator.mainIn();
            elevator.attachedIn();
            elevator.printEle(2);
        }
        while (elevator.getCurFloor() != elevator.getMainRequest().getDes()) {
            elevator.attachInOut();
            if (elevator.getDirection()) {
                elevator.arriveTo(elevator.getCurFloor() + 1);
            } else {
                elevator.arriveTo(elevator.getCurFloor() - 1);
            }
            if (elevator.isMaintain()) {
                return;
            }
        }
        elevator.printEle(1);
        elevator.passenOut(elevator.getMainRequest());
        elevator.setMainRequest(null);
        elevator.attachedOut();
        if (!elevator.getCurPersons().isEmpty()) {
            elevator.setMainRequest(elevator.getCurPersons().get(0));
            //System.out.println(elevator.getMainRequest().getId());
            elevator.attachedIn();
        } else {
            synchronized (requestQueueHashMap) {
                elevator.setMainRequest(this.setMainReq2(elevator));
            }
            if (elevator.getMainRequest() != null) {
                elevator.mainIn();
                elevator.attachedIn();
            }
        };
        elevator.printEle(2);
    }

    public Person setMainReq(Elevator elevator) {
        Person ansPerson = null;
        Person repPerson = null;
        for (ArrayList<Person> val : requestQueueHashMap.getRequestMap().values()) {
            for (Person person : val) {
                if (elevator.arriveAble(person.getDes()) && elevator.arriveAble(person.getSrc())) {
                    if (((person.getSrc() >= elevator.getCurFloor() && elevator.getDirection()
                            && (person.getDes() - person.getSrc() > 0)) ||
                            (person.getSrc() <= elevator.getCurFloor() && !elevator.getDirection()
                                    && (sub(person.getDes(), person.getSrc()) < 0)))
                            && !person.getMain()) {
                        if (elevator.arriveAble(person.getDes())) {
                            if (ansPerson == null) {
                                ansPerson = person;
                            } else {
                                if (Math.abs(sub(ansPerson.getSrc(), elevator.getCurFloor())) >
                                        Math.abs(sub(person.getSrc(), elevator.getCurFloor()))) {
                                    ansPerson = person;
                                }
                            }
                        }
                    }
                    if (!person.getMain()) {
                        if (repPerson == null) {
                            repPerson = person;
                        } else if (Math.abs(sub(repPerson.getSrc(), elevator.getCurFloor())) >
                                Math.abs(sub(person.getSrc(), elevator.getCurFloor()))) {
                            repPerson = person;
                        }
                    }
                }
            }
        }
        ansPerson = (ansPerson == null) ? repPerson : ansPerson;
        if (ansPerson != null) {
            ansPerson.setMain(true);
        }
        return ansPerson;
    }

    public boolean nextDirect(Elevator elevator) {
        if (elevator.getCurFloor() < elevator.getMainRequest().getSrc()) {
            return true;
        } else if (elevator.getCurFloor() > elevator.getMainRequest().getSrc()) {
            return false;
        } else {
            if (elevator.getMainRequest().getDes() > elevator.getMainRequest().getSrc()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Person setMainReq2(Elevator elevator) {
        Person ansPerson = null;
        Person repPerson = null;
        if (requestQueueHashMap.getRequestMap().containsKey(elevator.getCurFloor())) {
            for (Person person : requestQueueHashMap.getRequestMap().get(elevator.getCurFloor())) {
                if (elevator.arriveAble(person.getDes())) {
                    if (((elevator.getDirection() && (person.getDes() > person.getSrc())) ||
                            (!elevator.getDirection() && (person.getDes() < person.getSrc())))
                            && !person.getMain()) {
                        ansPerson = person;
                        break;
                    }
                    if (repPerson == null && !person.getMain()) {
                        repPerson = person;
                    } else if (repPerson != null && !person.getMain()) {
                        if (Math.abs(sub(repPerson.getDes(), repPerson.getSrc())) >
                                Math.abs(sub(person.getDes(), person.getSrc()))) {
                            if (!person.getMain()) {
                                repPerson = person;
                            }
                        }
                    }
                }
            }
        }

        ansPerson = (ansPerson == null) ? repPerson : ansPerson;
        if (ansPerson != null) {
            ansPerson.setMain(true);
        }
        return ansPerson;
    }

    public boolean isExit(Elevator elevator) {
        if (requestQueueHashMap.isOver() && requestQueueHashMap.isNull2()
                && elevator.getCurPersons().isEmpty() && elevator.getMainRequest() == null) {
            return true;
        }
        return false;
    }

    public int sub(int a, int b) {
        return a - b;
    }
}
