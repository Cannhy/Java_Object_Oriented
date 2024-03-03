import java.util.ArrayList;
import java.util.HashMap;

public class Maintainer {
    private static HashMap<Integer, Elevator> elevators;
    private int [][] access = new int[12][12];
    private int [][] val = new int[12][12];
    private int [][] path = new int[12][12];
    private int normalNum = 6;

    public Maintainer() {
        elevators = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            for (int j = 1; j < 12; j++) {
                access[i][j] = (i == j) ? 0 : 1;
                path[i][j] = j;
                val[i][j] = 6;
            }
        }
    }

    public synchronized HashMap<Integer, Elevator> getElevators() {
        return elevators;
    }

    public void putEle(Integer integer, Elevator elevator) {
        this.elevators.put(integer, elevator);
    }

    public static boolean haveExtraPas() {
        for (Elevator elevator : elevators.values()) {
            if (!elevator.getCurPersons().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public synchronized void freshMaintain(int id) {
        boolean flag = false;
        for (int i = 1; i <= 11; i++) {
            if (!elevators.get(id).arriveAble(i)) {
                flag = true;
            }
        }
        if (!flag) {
            normalNum--;
        }
        Elevator temp = elevators.get(id);
        for (int i = 1; i <= 11; i++) {
            if (temp.arriveAble(i)) {
                for (int j = 1; j <= 11; j++) {
                    if (temp.arriveAble(j) && i != j) {
                        val[i][j]--;
                        if (val[i][j] == 0) {
                            access[i][j] = 114514;
                        }
                    }
                }
            }
        }
        Floyd();
    }

    public synchronized void freshAdd(int id) {
        boolean flag = false;
        for (int i = 1; i <= 11; i++) {
            if (!elevators.get(id).arriveAble(i)) {
                flag = true;
            }
        }
        if (!flag) {
            normalNum++;
        }
        Elevator temp = elevators.get(id);
        for (int i = 1; i <= 11; i++) {
            if (temp.arriveAble(i)) {
                for (int j = 1; j <= 11; j++) {
                    if (i != j && temp.arriveAble(j)) {
                        val[i][j]++;
                        access[i][j] = 1;
                    }
                }
            }
        }
        Floyd();
    }

    public void Floyd() {
        for (int k = 1; k <= 11; k++) {
            for (int i = 1; i <= 11; i++) {
                for (int j = 1; j <= 11; j++) {
                    if (access[i][j] > access[i][k] + access[k][j]) {
                        access[i][j] = access[i][k] + access[k][j];
                        path[i][j] = path[i][k];
                    }
                }
            }
        }
    }

    public void setRoute(Person person) {
        ArrayList<Integer> ans = person.getNextDes();
        ans.clear();
        int start = person.getSrc();
        while (start != path[start][person.getFinalDes()]) {
            start = path[start][person.getFinalDes()];
            ans.add(start);
        }
    }

    public int getNormalNum() {
        return normalNum;
    }
}
