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
    private final int moveTime; //移动一层花费的时间
    private static final int maxNum = 6;

    public Elevator(RequestQueue requestQueueHashMap, int id) {
        this.requestQueueHashMap = requestQueueHashMap;
        this.curFloor = 1;
        this.id = id;
        this.direction = true;
        this.openTime = 200;
        this.closeTime = 200;
        this.moveTime = 400;
        this.mainRequest = null;
        this.scheduler = new Scheduler(requestQueueHashMap);
        this.curPersons = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            // 先判断电梯是否需要启动
            synchronized (requestQueueHashMap) {
                if (scheduler.isExit(this)) {
                    break;
                }
                if (requestQueueHashMap.isNull() && curPersons.isEmpty() && mainRequest == null) {
                    try {
                        requestQueueHashMap.wait();
                        if (scheduler.isExit(this)) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            // 启动，调度look算法进行载客
            scheduler.look(this);
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
        boolean flag = false;
        for (Person person : curPersons) {
            if (person.getDes() == curFloor) {
                flag = true;
                printEle(1);
                attachedOut();
                break;
            }
        }
        synchronized (requestQueueHashMap) {
            if (requestQueueHashMap.getRequestMap().containsKey(curFloor)) {
                for (Person person : requestQueueHashMap.getRequestMap().get(curFloor)) {
                    if (takeAble(person)) {
                        if (!flag) {
                            printEle(1);
                        }
                        attachedIn();
                        flag = true;
                        break;
                    }
                }
            }
        }
        if (flag) {
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
            Output.print(out);
            try {
                sleep(openTime + closeTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (option == 2) {
            out = "CLOSE-" + this.curFloor + "-" + this.id;
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
            if (curFloor < des) {
                this.curFloor++;
            } else {
                this.curFloor--;
            }
            work(moveTime);
            printEle(3);
        }
    }

    public void work(int time) {
        try {
            Thread.sleep(time);
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
}
