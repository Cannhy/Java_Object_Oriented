import java.util.ArrayList;

public class Elevator extends Thread {
    private final RequestQueue requestQueueHashMap; //共享队列
    private ArrayList<Person> curPersons; //电梯内部的人员情况
    private Scheduler scheduler; // 策略
    private Person mainRequest;
    private int id;
    private int curFloor;
    private boolean direction;
    private final int openTime;
    private final int closeTime;
    private final double moveTime; //移动一层花费的时间
    private final int maxNum;
    private boolean doorFlag;
    private boolean isMaintain = false;
    private final int access;
    private boolean onlyRec;
    private Maintainer maintainer;
    private int cnt = 0;

    public Elevator(RequestQueue requestQueueHashMap,
                    int id, int curFloor, int max, double moveTime,
                    int arriveAble, Maintainer maintainer) {
        this.requestQueueHashMap = requestQueueHashMap;
        this.id = id;
        this.curFloor = curFloor;
        this.maxNum = max;
        this.moveTime = moveTime * 1000;
        this.direction = true;
        this.openTime = 200;
        this.closeTime = 200;
        this.mainRequest = null;
        this.scheduler = new Scheduler(requestQueueHashMap);
        this.curPersons = new ArrayList<>();
        this.doorFlag = false;
        this.access = arriveAble;
        this.maintainer = maintainer;
    }

    @Override
    public void run() {
        while (true) {
            // 先判断电梯是否需要启动
            //System.out.println("run" + id);
            //System.out.println(n++);
            boolean tempFlag = false;
            synchronized (requestQueueHashMap) {
                if (scheduler.isExit(this) && !Maintainer.haveExtraPas()) {
                    //System.out.println(id + "exit");
                    requestQueueHashMap.notifyAll();
                    break;
                }
                if ((requestQueueHashMap.isNull() && curPersons.isEmpty() && mainRequest == null)
                        || (!this.nb() && (maintainer.getNormalNum() != 0
                        || (this.mainRequest == null && cnt != 0)))) {
                    try {
                        //System.out.println("waiting" + id);
                        requestQueueHashMap.wait();
                        //System.out.println("waitingOK" + id);
                        if (this.isMaintain) {
                            this.eleMaintain();
                            break;
                        }
                        if (scheduler.isExit(this) && !Maintainer.haveExtraPas()) {
                            //System.out.println(id + "exit");
                            requestQueueHashMap.notifyAll();
                            break;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!requestQueueHashMap.isNull() || !curPersons.isEmpty()) {
                    tempFlag = true;
                }
            }
            // 启动，调度look算法进行载客
            if (tempFlag) {
                scheduler.look(this);
                cnt++;
            }
            if (this.isMaintain) {
                this.eleMaintain();
                break;
            }
        }
    }

    public void mainIn() {
        passenIn(mainRequest);
        mainRequest.setMain(false);
        this.direction = scheduler.nextDirect(this);
    }

    public void attachedIn() {
        synchronized (requestQueueHashMap) {
            if (requestQueueHashMap.getRequestMap().containsKey(curFloor)) {
                for (int i = 0; i < requestQueueHashMap.getRequestMap().get(curFloor).size(); i++) {
                    if (takeAble(requestQueueHashMap.getRequestMap().get(curFloor).get(i))) {
                        passenIn(requestQueueHashMap.getRequestMap().get(curFloor).get(i));
                        i--;
                    }
                }
            }
        }
    }

    public void attachedOut() {
        synchronized (requestQueueHashMap) {
            for (int i = 0; i < curPersons.size(); i++) {
                if (curPersons.get(i).getDes() == curFloor) {
                    //passenOut(curPersons.get(i));
                    if (curPersons.get(i).getFinalDes() != curFloor) {
                        curPersons.get(i).setSrc(curFloor);
                        curPersons.get(i).setDes();
                        requestQueueHashMap.addPerson(curFloor, curPersons.get(i));
                    }
                    printPas(2, curPersons.get(i));
                    curPersons.remove(curPersons.get(i));
                    i--;
                }
            }
            requestQueueHashMap.notifyAll();
        }
    }

    public void attachInOut() {
        boolean flag = false;
        if (this.arriveAble(curFloor)) {
            for (Person person : curPersons) {
                if (person.getDes() == curFloor && this.arriveAble(person.getDes())) {
                    //flag = true;
                    this.doorFlag = true;
                    printEle(1);
                    attachedOut();
                    break;
                }
            }
            synchronized (requestQueueHashMap) {
                if (requestQueueHashMap.getRequestMap().containsKey(curFloor)) {
                    for (Person person : requestQueueHashMap.getRequestMap().get(curFloor)) {
                        if (takeAble(person)) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (flag) {
                if (!this.doorFlag) {
                    printEle(1);
                }
                attachedIn();
                this.doorFlag = true;
            }
            attachedIn();
            if (this.doorFlag) {
                printEle(2);
            }
        }
    }

    public boolean takeAble(Person person) {
        if (!person.getMain() && !this.isFull() && this.arriveAble(person.getDes())) {
            return (direction && person.getDes() > person.getSrc()) ||
                    !direction && person.getDes() < person.getSrc();
        }
        return false;
    }

    public synchronized boolean isFull() {
        return this.curPersons.size() == maxNum;
    }

    public void printEle(int option) {
        String out = null;
        if (option == 1) {
            synchronized (DoorLock.getLocks().get(this.curFloor)) {
                if (DoorLock.getServing()[curFloor] == 4) {
                    try {
                        DoorLock.getLocks().get(curFloor).wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (this.onlyReceive()) {
                        while (DoorLock.getOnlyRev()[curFloor] == 2) {
                            try {
                                DoorLock.getLocks().get(curFloor).wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        DoorLock.getOnlyRev()[curFloor]++;
                    }
                    DoorLock.getServing()[curFloor]++;
                }
            }
            out = "OPEN-" + this.curFloor + "-" + this.id;
            this.doorFlag = true;
            Output.print(out);
            try {
                sleep(openTime + closeTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (option == 2) {
            out = "CLOSE-" + this.curFloor + "-" + this.id;
            this.doorFlag = false;
            Output.print(out);
            synchronized (DoorLock.getLocks().get(this.curFloor)) {
                DoorLock.getServing()[curFloor]--;
                if (this.onlyRec) {
                    DoorLock.getOnlyRev()[curFloor]--;
                }
                this.setOnlyRec(false);
                DoorLock.getLocks().get(this.curFloor).notifyAll();
            }
        } else if (option == 3) {
            out = "ARRIVE-" + this.curFloor + "-" + this.id;
            Output.print(out);
        }
    }

    public void printPas(int option, Person person) {
        String out;
        if (option == 1) {
            out = "IN-" + person.getId() + "-" + this.curFloor + "-" + this.id;
        } else {
            out = "OUT-" + person.getId() + "-" + this.curFloor + "-" + this.id;
        }
        Output.print(out);
    }

    public void passenIn(Person person) {
        synchronized (requestQueueHashMap) {
            requestQueueHashMap.delPerson(person);
        }
        curPersons.add(person);
        printPas(1, person);
    }

    public void passenOut(Person person) {
        curPersons.remove(person);
        if (curFloor != person.getFinalDes()) {
            person.setSrc(curFloor);
            person.setDes();
            synchronized (requestQueueHashMap) {
                requestQueueHashMap.addPerson(curFloor, person);
                requestQueueHashMap.notifyAll();
            }
        }
        printPas(2, person);
    }

    public void arriveTo(int des) {
        while (curFloor != des) {
            if (this.isMaintain) {
                //System.out.println("ina");
                return;
            }
            if (curFloor < des) {
                this.curFloor++;
            } else {
                this.curFloor--;
            }
            work(moveTime);
            printEle(3);
        }
    }

    public boolean onlyReceive() {
        if (this.isMaintain) {
            return false;
        }
        for (Person person : curPersons) {
            if (person.getDes() == curFloor && this.arriveAble(person.getDes())) {
                this.onlyRec = false;
                return false;
            }
        }
        this.onlyRec = true;
        return true;
    }

    public void eleMaintain() {
        maintainer.freshMaintain(this.id);
        maintainer.getElevators().remove(this.id);
        if (this.getMainRequest() != null) {
            this.getMainRequest().setMain(false);
        }
        if (!this.doorFlag && !this.getCurPersons().isEmpty()) {
            printEle(1);
        }
        synchronized (requestQueueHashMap) {
            for (ArrayList<Person> array : requestQueueHashMap.getRequestMap().values()) {
                for (Person person : array) {
                    //System.out.println(person.getId() + " " + person.getSrc() +
                    // " " + person.getDes());
                    maintainer.setRoute(person);
                    person.setDes();
                    /*for (Integer temp : person.getNextDes()) {
                        System.out.println(temp);
                    }*/
                }
            }
            if (this.getCurPersons().isEmpty()) {
                Output.print("MAINTAIN_ABLE-" + id);
                requestQueueHashMap.notifyAll();
                return;
            }
            for (int i = 0; i < curPersons.size(); i++) {
                if (curPersons.get(i).getDes() != curFloor) {
                    if (curPersons.get(i).getFinalDes() == curFloor) {
                        printPas(2, curPersons.get(i));
                        curPersons.remove(curPersons.get(i));
                        continue;
                    }
                    curPersons.get(i).setMain(false);
                    curPersons.get(i).setSrc(curFloor);
                    maintainer.setRoute(curPersons.get(i));
                    curPersons.get(i).setDes();
                    requestQueueHashMap.addPerson(curFloor, curPersons.get(i));
                    printPas(2, curPersons.get(i));
                    curPersons.remove(curPersons.get(i));
                } else {
                    passenOut(curPersons.get(i));
                }
                //passenOut(curPersons.get(i));
                i--;
            }
            requestQueueHashMap.notifyAll();
        }
        printEle(2);
        Output.print("MAINTAIN_ABLE-" + id);
    }

    public boolean nb() {
        for (int i = 1; i <= 11; i++) {
            if (!this.arriveAble(i)) {
                return false;
            }
        }
        return true;
    }

    public void work(double time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean arriveAble(int floor) {
        return (access & (1 << (floor - 1))) != 0;
    }

    public int getCurFloor() {
        return curFloor;
    }

    public boolean getDirection() {
        return direction;
    }

    public Person getMainRequest() {
        return mainRequest;
    }

    public void setMainRequest(Person mainRequest) {
        this.mainRequest = mainRequest;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public ArrayList<Person> getCurPersons() {
        return curPersons;
    }

    public boolean isMaintain() {
        return isMaintain;
    }

    public void setMaintain(boolean maintain) {
        isMaintain = maintain;
    }

    public void setOnlyRec(boolean onlyRec) {
        this.onlyRec = onlyRec;
    }

    public int gettId() {
        return id;
    }
}
