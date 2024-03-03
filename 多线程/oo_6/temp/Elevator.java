import java.util.ArrayList;

public class Elevator extends Thread {
    private final RequestQueue requestQueueHashMap; //共享队列
    private ArrayList<Person> curPersons; //电梯内部的人员情况
    private Scheduler scheduler; // 策略
    private Person mainRequest;
    private final int id;
    private int curFloor;
    private boolean direction;
    private final int openTime;
    private final int closeTime;
    private final double moveTime; //移动一层花费的时间
    private final int maxNum;
    private boolean doorFlag;
    private boolean isMaintain = false;

    public Elevator(RequestQueue requestQueueHashMap,
                    int id, int curFloor, int max, double moveTime) {
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
    }

    @Override
    public void run() {
        while (true) {
            // 先判断电梯是否需要启动
            boolean tempFlag = false;
            synchronized (requestQueueHashMap) {
                if (scheduler.isExit(this) && !Maintainer.haveExtraPas()) {
                    //System.out.println(id + "exit");
                    requestQueueHashMap.notifyAll();
                    break;
                }
                if (requestQueueHashMap.isNull() && curPersons.isEmpty() && mainRequest == null) {
                    try {
                        requestQueueHashMap.wait();
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
                if (!requestQueueHashMap.isNull()) {
                    tempFlag = true;
                }
            }
            // 启动，调度look算法进行载客
            if (tempFlag) {
                scheduler.look(this);
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
        for (int i = 0; i < curPersons.size(); i++) {
            if (curPersons.get(i).getDes() == curFloor) {
                passenOut(curPersons.get(i));
                i--;
            }
        }
    }

    public void attachInOut() {
        //boolean flag = false;
        for (Person person : curPersons) {
            if (person.getDes() == curFloor) {
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
                        if (!this.doorFlag) {
                            printEle(1);
                        }
                        attachedIn();
                        //flag = true;
                        this.doorFlag = true;
                        break;
                    }
                }
            }
        }
        if (this.doorFlag) {
            printEle(2);
        }
    }

    public boolean takeAble(Person person) {
        if (!person.getMain() && !this.isFull()) {
            if ((direction && person.getDes() > person.getSrc()) ||
                    !direction && person.getDes() < person.getSrc()) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isFull() {
        return this.curPersons.size() == maxNum;
    }

    public void printEle(int option) {
        String out = null;
        if (option == 1) {
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

    public void eleMaintain() {
        if (this.getMainRequest() != null) {
            this.getMainRequest().setMain(false);
        }
        if (!this.doorFlag && !this.getCurPersons().isEmpty()) {
            printEle(1);
        }
        synchronized (requestQueueHashMap) {
            if (this.getCurPersons().isEmpty()) {
                Output.print("MAINTAIN_ABLE-" + id);
                requestQueueHashMap.notifyAll();
                return;
            }
            for (int i = 0; i < curPersons.size(); i++) {
                if (curPersons.get(i).getDes() != curFloor) {
                    curPersons.get(i).setMain(false);
                    curPersons.get(i).setSrc(curFloor);
                    requestQueueHashMap.addPerson(curFloor, curPersons.get(i));
                }
                passenOut(curPersons.get(i));
                i--;
            }
            requestQueueHashMap.notifyAll();
        }
        printEle(2);
        Output.print("MAINTAIN_ABLE-" + id);
    }

    public void work(double time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
}
